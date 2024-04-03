package com.crimeMap.Backend.Services.Alerts;

import com.crimeMap.Backend.DTO.Request.AlertRequest;
import com.crimeMap.Backend.Entities.Alerts;
import com.crimeMap.Backend.Entities.CrimeType;
import com.crimeMap.Backend.Entities.University;
import com.crimeMap.Backend.Repository.AlertsRepository;
import com.crimeMap.Backend.Repository.CrimeTypeRepository;
import com.crimeMap.Backend.Repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlertServiceImpl implements AlertService{

    @Autowired
    private AlertsRepository alertsRepository;
    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private CrimeTypeRepository crimeTypeRepository;

    @Override
    public void processAlertRequest(AlertRequest request) {
         System.out.println(request.toString());
         String universityName  = request.getUniversity();
         University university = universityRepository.findByName(universityName);
         String email = request.getEmail();

        // Save crimes
        for (Map.Entry<String, Boolean> entry : request.getCrimes().entrySet()) {
             String crimeName = entry.getKey();
             boolean isActive = entry.getValue();
            if (isActive) {
                Alerts alerts = new Alerts();
                alerts.setEmailId(email);
                alerts.setUniversity(university);
                alerts.setIsActive(true);
                CrimeType crimeType = crimeTypeRepository.findByDescriptionIgnoreCase(crimeName);
                alerts.setCrimeType(crimeType);
                alertsRepository.save(alerts);
            }
        }
    }
}
