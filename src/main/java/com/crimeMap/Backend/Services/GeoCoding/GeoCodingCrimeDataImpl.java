package com.crimeMap.Backend.Services.GeoCoding;


import com.crimeMap.Backend.DTO.Response.CrimeDetailsDTO;
import com.crimeMap.Backend.Entities.CrimeDetails;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GeoCodingCrimeDataImpl implements GeoCodingCrimeData {

    @Autowired
    private GeoCodingService geoCodingService;

    @Override
    public List<CrimeDetailsDTO> getDetailsWithCoordinates(List<CrimeDetails> crimeDetailsList) {
        List<CrimeDetailsDTO> crimeDetailsDTOList = new ArrayList<>();

        for (CrimeDetails crimeDetails : crimeDetailsList) {
            try {
                // Assuming the location in CrimeDetails is the address for geocoding
                GeocodingResult[] results = geoCodingService.geocode(convertCrimeLocation(crimeDetails.getLocation()) + " UMBC Baltimore, MD 21250");
                if (results.length > 0) {
                    GeocodingResult result = results[0]; // Taking the first result
                    CrimeDetailsDTO dto = new CrimeDetailsDTO();

                    // Copying properties from crimeDetails to dto
                    dto.setLocation(convertCrimeLocation(crimeDetails.getLocation()));
                    dto.setDateReported(crimeDetails.getDateReported());
                    dto.setCaseId(crimeDetails.getCaseId());
                    dto.setCrimeType(convertCrimeType(crimeDetails.getCrimeType()));
                    dto.setDateOccurred(crimeDetails.getDateOccurred());
                    dto.setStatusDisposition(crimeDetails.getStatusDisposition());
                    dto.setUpdated(crimeDetails.getUpdated());

                    // Setting latitude and longitude
                    dto.setLatitude(result.geometry.location.lat);
                    dto.setLongitude(result.geometry.location.lng);

                    crimeDetailsDTOList.add(dto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return crimeDetailsDTOList;
    }

    private String convertCrimeLocation(String location) {
        // Remove the text inside brackets (including the brackets themselves)
        location = location.replaceAll("\\s*\\(.*?\\)", "");
        // Remove hyphens and the phrase "On Campus"
        location = location.replace("-", "").replace("On Campus", "").replace("Bldg","Building").trim();

        return location;
    }


    private String convertCrimeType(String crimeType) {

        String[] crime = crimeType.split(" ");
        if(Objects.equals(crime[0], "MAL")){
            crime[0] = "MAL DESTRUCTION";
        }
        return crime[0];
    }
}