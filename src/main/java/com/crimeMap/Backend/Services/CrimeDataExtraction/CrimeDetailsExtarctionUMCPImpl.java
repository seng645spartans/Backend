package com.crimeMap.Backend.Services.CrimeDataExtraction;

import com.crimeMap.Backend.Entities.CrimeDetails;
import com.crimeMap.Backend.Entities.CrimeDetailsScheduler;
import com.crimeMap.Backend.Entities.CrimeType;
import com.crimeMap.Backend.Entities.University;
import com.crimeMap.Backend.Repository.CrimeDetailsRepository;
import com.crimeMap.Backend.Repository.CrimeDetailsSchedulerRepository;
import com.crimeMap.Backend.Repository.CrimeTypeRepository;
import com.crimeMap.Backend.Repository.UniversityRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("UMCP")
public class CrimeDetailsExtarctionUMCPImpl implements CrimeDetailsExtraction{

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    CrimeTypeRepository crimeTypeRepository;

    @Autowired
    CrimeDetailsRepository crimeDetailsRepository;

    @Autowired
    private CrimeDetailsSchedulerRepository crimeDetailsSchedulerRepository;

    @Override
    public List<CrimeDetails> getCrimeDetails(String universityName) throws IOException {

        if(schedulerCheck(universityName)){
            return returnCrimeDetailsFromDB(universityName);
        }
        String url = "https://www.umpd.umd.edu/stats/incident_logs.cfm?year=2024&month=5";
        Document doc = Jsoup.connect(url).get();

        // Select the first tbody element if there are multiple, or adjust if needed
        Element tbody = doc.select("tbody").first();
        List<CrimeDetails> crimeDetailsList = new ArrayList<>();

        if (tbody != null) {
            // Skip the first row and start iterating from the second row
            Elements rows = tbody.select("tr:gt(0)"); // :gt(0) skips the first element
            boolean isOddRow = false; // Flag to check if the row is odd
            University university = universityRepository.findByName("UMCP");
            for (Element row : rows) {
                Elements cells = row.select("td");
                CrimeDetails crimeDetails = new CrimeDetails();
                if (!isOddRow) {
                    // Process normal rows
                    if (cells.size() > 1) {
                        crimeDetails.setUniversity(university);
                        crimeDetails.setCaseId(cells.get(0).text());
                        crimeDetails.setDateOccurred(extracted(cells.get(1).text()));
                        crimeDetails.setDateReported(extracted(cells.get(2).text()));
                        setCrimeType(cells, crimeDetails);
                        crimeDetails.setStatusDisposition(cells.get(4).text());
                        System.out.println("Case Number: " + cells.get(0).text());
                        System.out.println("Occurred: " + extracted(cells.get(1).text()));
                        System.out.println("Reported: " + extracted(cells.get(2).text()));
                        System.out.println("Type: " + cells.get(3).text());
                        System.out.println("Disposition: " + cells.get(4).text());
                        crimeDetailsList.add(crimeDetails);
                    }
                    isOddRow = true; // Next row will be an odd row
                } else {
                    // Process odd rows for addresses
                    if (!crimeDetailsList.isEmpty() && cells.size() == 1 ) {
                        crimeDetails = crimeDetailsList.get(crimeDetailsList.size()-1);
                        crimeDetails.setLocation(cells.get(0).text());
                        System.out.println("Address: " + cells.get(0).text());
                    }
                    isOddRow = false; // Next row will be a normal row
                }

            }
            crimeDetailsRepository.saveAll(crimeDetailsList);
        }
        return crimeDetailsList;
    }

    private void setCrimeType(Elements cells, CrimeDetails crimeDetails) {
        CrimeType crimeType = crimeTypeRepository.findByDescription(cells.get(3).text());
        if(!Objects.isNull(crimeType)){
            crimeDetails.setCrimeTypeID(crimeType);
        }
        else {
            crimeType = crimeTypeRepository.findByDescription("Other");
            crimeDetails.setCrimeTypeID(crimeType);
        }
        crimeDetails.setCrimeType(cells.get(3).text());
    }

    private static Timestamp extracted(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
        return Timestamp.valueOf(dateTime);
    }

    private boolean schedulerCheck(String universityName) {
        Optional<CrimeDetailsScheduler> crimeDetailsSchedulerOptional = crimeDetailsSchedulerRepository.
                findFirstByUniversityOrderByLastScheduledRunDesc(universityName);

        if (crimeDetailsSchedulerOptional.isPresent()) {
            CrimeDetailsScheduler crimeDetailsScheduler = crimeDetailsSchedulerOptional.get();
            return isWithin24Hours(crimeDetailsScheduler.getLastScheduledRun(), new Timestamp(System.currentTimeMillis()));
        } else {
            return false;
        }
    }

    public boolean isWithin24Hours(Timestamp timestamp1, Timestamp timestamp2) {
        long diffInMilliseconds = Math.abs(timestamp2.getTime() - timestamp1.getTime());
        long diffInHours = diffInMilliseconds / (60 * 60 * 1000); // Convert milliseconds to hours
        return diffInHours <= 24;
    }

    private List<CrimeDetails> returnCrimeDetailsFromDB(String universityName) {
        University university = universityRepository.findByName(universityName);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ninetyDaysAgoDateTime = now.minusDays(90);
        Timestamp ninetyDaysAgo = Timestamp.valueOf(ninetyDaysAgoDateTime);
        return crimeDetailsRepository.
                findByUniversityAndDateReportedAfterAndCrimeTypeActive(university,ninetyDaysAgo);
    }
}
