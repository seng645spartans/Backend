package com.crimeMap.Backend.Services.CrimeDataExtraction;

import com.crimeMap.Backend.Entities.CrimeDetails;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("UMBC")
public class CrimeDetailsExtarctionUMBCImpl implements CrimeDetailsExtraction{
    @Override
    public List<CrimeDetails> getCrimeDetails(String universityName) {
        File file = getCrimePdf();
        List<CrimeDetails> crimeDetailsList = new ArrayList<>();

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            Pattern pattern = Pattern.compile("(?m)(^[^\\n]*\\n)?(Date Reported:.*?Modified Date:.*?(?=\\nDate Reported|$))", Pattern.MULTILINE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String matchedText = matcher.group(1) != null ? matcher.group(1) + matcher.group(2) : matcher.group(2);
                CrimeDetails report = getCrimeDetailsFromPDF(matchedText);
                if (report != null) {
                    crimeDetailsList.add(report);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            file.delete();
        }
        return crimeDetailsList;
    }
    private static File getCrimePdf(){
        String url = "https://police.umbc.edu/crime/";
        String pdfURL = null;
        File file = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements newsHeadlines = doc.getElementsByClass("sights-callout-box sights-p-4 mceNonEditable");
            for (Element headline : newsHeadlines) {
                pdfURL = headline.getElementsByClass("mceEditable").select("a").first().attr("href");
                if (!pdfURL.isEmpty()) {
                    break;
                }
            }

            if (pdfURL != null) {
                downloadPdf(pdfURL, "downloaded_pdf.pdf");
                file = new File("downloaded_pdf.pdf");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static CrimeDetails getCrimeDetailsFromPDF(String matchedText) {
        String[] lines = matchedText.split("\\n");
        CrimeDetails report = new CrimeDetails();

        try {
            if (lines.length < 12 && lines[0].contains("Date Reported:")) {
                // Assuming lines have appropriate content before accessing
                if (lines.length >= 9) {
                    report.setDateReported(extracted(convertDate(lines[0].replace("Date Reported: ", "").replace("Report #:", "").trim())));
                    report.setLocation(lines[1].trim());
                    report.setCaseId(lines[2].trim());
                    report.setCrimeType(lines[4].replace("Incident/Offenses: ", "").trim());
                    report.setDateOccurred(extracted(convertDate(lines[6].trim())));
                    report.setStatusDisposition(lines[7].replace("Disposition:", "").trim());
                    report.setUpdated(extracted(convertDate(lines[8].replace("Modified Date: ", "").trim())));
                }
            } else if (lines.length == 12) {
                report.setDateReported(extracted(convertDate(lines[0].replace("Date Reported: ", "").replace("Report #:", "").trim())));
                report.setLocation(lines[6].trim());
                report.setCaseId(lines[2].trim());
                report.setCrimeType(lines[9].replace("Incident/Offenses: ", "").trim());
                report.setDateOccurred(extracted(convertDate(lines[7].trim())));
                report.setStatusDisposition(lines[10].replace("Disposition:", "").trim());
                report.setUpdated(extracted(convertDate(lines[11].replace("Modified Date: ", "").trim())));
            } else {
                // Type 1 format
                if (lines.length >= 10) {
                    report.setLocation(lines[0].trim());
                    report.setDateReported(extracted(convertDate(lines[1].replace("Date Reported: ", "").replace("Report #:", "").trim())));
                    report.setCaseId(lines[3].trim());
                    report.setCrimeType(lines[5].replace("Incident/Offenses: ", "").trim());
                    report.setDateOccurred(extracted(convertDate(lines[7].trim())));
                    report.setStatusDisposition(lines[8].replace("Disposition:", "").trim());
                    report.setUpdated(extracted(convertDate(lines[9].replace("Modified Date: ", "").trim())));
                }
            }
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date in text: " + matchedText);
            return null;
        }

        return report;
    }
    private static void downloadPdf(String pdfURL, String destination) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(pdfURL);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            InputStream inputStream = response.getEntity().getContent();
            Path targetPath = new File(destination).toPath();
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Timestamp extracted(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy 'at' HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
        return Timestamp.valueOf(dateTime);
    }

    public static String convertDate(String inputDate) {
        String regex = "(\\d{2}/\\d{2}/\\d{2}) - [A-Za-z]{3} at (\\d{2}:\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputDate);

        if (matcher.find()) {
            return matcher.group(1) + " at " + matcher.group(2);
        }
        return inputDate;
    }
}