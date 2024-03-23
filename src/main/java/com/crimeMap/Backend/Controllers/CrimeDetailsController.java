package com.crimeMap.Backend.Controllers;

import com.crimeMap.Backend.DTO.Response.CrimeDetailsDTO;
import com.crimeMap.Backend.Entities.CrimeDetails;
import com.crimeMap.Backend.Services.CrimeDataExtraction.CrimeDetailsExtraction;
import com.crimeMap.Backend.Services.GeoCoding.GeoCodingCrimeData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/getCrimeData")
public class CrimeDetailsController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    GeoCodingCrimeData geoCodingCrimeData;

    @GetMapping("/{universityName}")
    public ResponseEntity<List<CrimeDetailsDTO>> getCrimeDetails(@PathVariable String universityName) {
        CrimeDetailsExtraction crimeDetailsExtraction;

        try {
            crimeDetailsExtraction = (CrimeDetailsExtraction) context.getBean(universityName);
        } catch (BeansException e) {
            return ResponseEntity.notFound().build();
        }

        List<CrimeDetails> crimeDetails = crimeDetailsExtraction.getCrimeDetails(universityName);
        List<CrimeDetailsDTO> crimeDetailsDTOList = geoCodingCrimeData.getDetailsWithCoordinates(crimeDetails);
        return ResponseEntity.ok(crimeDetailsDTOList);
    }
}
