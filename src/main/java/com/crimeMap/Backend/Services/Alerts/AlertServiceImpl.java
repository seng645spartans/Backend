package com.crimeMap.Backend.Services.Alerts;

import com.crimeMap.Backend.DTO.Request.AlertRequest;
import com.crimeMap.Backend.Entities.Alerts;
import com.crimeMap.Backend.Entities.CrimeDetails;
import com.crimeMap.Backend.Entities.CrimeType;
import com.crimeMap.Backend.Entities.University;
import com.crimeMap.Backend.Repository.*;
import com.crimeMap.Backend.Services.Mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class AlertServiceImpl implements AlertService{

    @Autowired
    private AlertsRepository alertsRepository;
    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private CrimeTypeRepository crimeTypeRepository;

    @Autowired
    private AlertSchedulerRepository alertsSchedulerRepository;

    @Autowired
    private MailService emailService;

    @Autowired
    private CrimeDetailsRepository crimeDetailsRepository;

    @Override
    public void processAlertRequest(AlertRequest request) {
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
                System.out.println(crimeName);
                System.out.println("Crime Type" + alerts.getCrimeType());
                alertsRepository.save(alerts);
            }
        }
    }

    @Override
    public void triggerAlertForUniversity(){
        List<University> universities = universityRepository.findAll();
        for(University university :  universities){
            processAlertsForUniversity(university.getId());
        }
    }

    private void processAlertsForUniversity(Long universityId) {
        Timestamp lastSchedulerRun = alertsSchedulerRepository.findTopByOrderByLastSchedulerRunDesc().getLastSchedulerRun();

        List<Alerts> activeAlerts = alertsRepository.findByIsActiveTrueAndUniversityId(universityId);
        List<CrimeDetails> newCrimes = crimeDetailsRepository.findByCreatedAtAfterAndUniversityId(lastSchedulerRun, universityId);

        for (Alerts alert : activeAlerts) {
            for (CrimeDetails crime : newCrimes) {
                // Check if the crime matches the alert criteria (e.g., crime type)
                if (alert.getCrimeType().equals(crime.getCrimeTypeID())) {
                    // Send email notification
                    emailService.sendSimpleMessage(alert.getEmailId(), "Crime Alert", crime.getCrimeType() + " " + crime.getLocation());
                }
            }
        }
        // Update the last scheduler run timestamp after processing
        alertsSchedulerRepository.updateLastSchedulerRun(new Timestamp(System.currentTimeMillis()));
    }
}
