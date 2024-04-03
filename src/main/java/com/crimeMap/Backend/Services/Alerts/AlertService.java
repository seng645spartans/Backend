package com.crimeMap.Backend.Services.Alerts;

import com.crimeMap.Backend.DTO.Request.AlertRequest;

public interface AlertService {
    public void processAlertRequest(AlertRequest request);
}
