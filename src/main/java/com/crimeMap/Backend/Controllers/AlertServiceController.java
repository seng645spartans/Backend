package com.crimeMap.Backend.Controllers;

import com.crimeMap.Backend.DTO.Request.AlertRequest;
import com.crimeMap.Backend.Services.Alerts.AlertService;
import com.crimeMap.Backend.Services.GeoCoding.GeoCodingService;
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

    @Autowired
    private GeoCodingService geoCodingService;

    @PostMapping("/new")
    public ResponseEntity<String> handleAlertRequest(@RequestBody AlertRequest request) {
        // Call the service method to process the alert request
        alertService.processAlertRequest(request);

        return new ResponseEntity<>("Alert request processed successfully", HttpStatus.OK);
    }

    @GetMapping("/evict")
    public ResponseEntity<String> evict() {

        geoCodingService.cacheEvict();

        return new ResponseEntity<>("Cache eviction processed successfully", HttpStatus.OK);
    }

    @GetMapping("/mail")
    public ResponseEntity<String> mail() {

        alertService.triggerAlertForUniversity();

        return new ResponseEntity<>("Email request processed successfully", HttpStatus.OK);
    }
}
