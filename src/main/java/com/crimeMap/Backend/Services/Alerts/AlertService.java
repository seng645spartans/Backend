package com.crimeMap.Backend.Services.Alerts;

import com.crimeMap.Backend.DTO.Request.AlertRequest;

public interface AlertService {
    void processAlertRequest(AlertRequest request);

    void triggerAlertForUniversity();
}
