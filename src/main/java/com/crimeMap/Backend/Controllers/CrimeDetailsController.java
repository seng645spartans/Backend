package com.crimeMap.Backend.Controllers;

import com.crimeMap.Backend.DTO.Response.CrimeDetailsDTO;
import com.crimeMap.Backend.DTO.Response.CrimeDetailsInfoDTO;
import com.crimeMap.Backend.DTO.Response.CrimeTypeDTO;
import com.crimeMap.Backend.Entities.CrimeDetails;
import com.crimeMap.Backend.Repository.CrimeDetailsRepository;
import com.crimeMap.Backend.Repository.CrimeTypeRepository;
import com.crimeMap.Backend.Services.CrimeDataExtraction.CrimeDetailsExtraction;
import com.crimeMap.Backend.Services.CrimeTypeService.CrimeTypeService;
import com.crimeMap.Backend.Services.GeoCoding.GeoCodingCrimeData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/getCrimeData")
public class CrimeDetailsController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    GeoCodingCrimeData geoCodingCrimeData;

    @Autowired
    CrimeDetailsRepository crimeDetailsRepository;

    @Autowired
    CrimeTypeRepository crimeTypeRepository;

    @Autowired
    private CrimeTypeService crimeTypeService;

    @GetMapping("/{universityName}")
    public ResponseEntity<List<CrimeDetailsDTO>> getCrimeDetails(@PathVariable String universityName) throws IOException {
        CrimeDetailsExtraction crimeDetailsExtraction;

        try {
            crimeDetailsExtraction = (CrimeDetailsExtraction) context.getBean(universityName);
        } catch (BeansException e) {
            return ResponseEntity.notFound().build();
        }

        List<CrimeDetails> crimeDetails = crimeDetailsExtraction.getCrimeDetails(universityName);
        List<CrimeDetailsDTO> crimeDetailsDTOList = geoCodingCrimeData.getDetailsWithCoordinates(crimeDetails,universityName);
        return ResponseEntity.ok(crimeDetailsDTOList);
    }

    @GetMapping("/Info/{caseId}")
    public ResponseEntity<CrimeDetailsInfoDTO> getCrimeDetailsInfo(@PathVariable String caseId) {
         CrimeDetails crimeDetails = crimeDetailsRepository.getByCaseId(caseId);

         CrimeDetailsInfoDTO crimeDetailsDTO = new CrimeDetailsInfoDTO();
         crimeDetailsDTO.setType(crimeDetails.getCrimeType());
         crimeDetailsDTO.setDate(String.valueOf(crimeDetails.getDateOccurred()));
         crimeDetailsDTO.setDescription(crimeDetails.getDetails());
         crimeDetailsDTO.setAddress(crimeDetails.getLocation());

        return ResponseEntity.ok(crimeDetailsDTO);
    }

    @GetMapping("/crimeTypes")
    public ResponseEntity<?> getCrimeType(){
        List<String> crimes = crimeTypeRepository.findActiveCrimeDescriptions();
        return ResponseEntity.ok(crimes);
    }

    @PostMapping("/updateCrimeTypes")
    public ResponseEntity<?> updateCrimeTypes(@RequestBody List<CrimeTypeDTO> crimeTypes) {
        try {
            crimeTypeService.updateCrimeTypes(crimeTypes);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Crime types updated successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating crime types: " + e.getMessage());
        }
    }

    @GetMapping("/crimeTypesWithStatus")
    public ResponseEntity<List<CrimeTypeDTO>> getCrimeTypesWithStatus() {
        try {
            List<CrimeTypeDTO> crimeTypes = crimeTypeService.findAllCrimeTypes();
            return ResponseEntity.ok(crimeTypes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
