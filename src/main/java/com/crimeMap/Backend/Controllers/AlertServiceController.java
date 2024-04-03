package com.crimeMap.Backend.Controllers;

import com.crimeMap.Backend.DTO.Request.AlertRequest;
import com.crimeMap.Backend.Services.Alerts.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Alerts")
public class AlertServiceController {

    @Autowired
    private AlertService alertService; // Assuming you have a service class to handle business logic

    @PostMapping("/new")
    public ResponseEntity<String> handleAlertRequest(@RequestBody AlertRequest request) {
        // Call the service method to process the alert request
        alertService.processAlertRequest(request);

        return new ResponseEntity<>("Alert request processed successfully", HttpStatus.OK);
    }
}
