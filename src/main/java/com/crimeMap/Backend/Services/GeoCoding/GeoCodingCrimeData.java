package com.crimeMap.Backend.Services.GeoCoding;

import com.crimeMap.Backend.DTO.Response.CrimeDetailsDTO;
import com.crimeMap.Backend.Entities.CrimeDetails;

import java.util.List;

public interface GeoCodingCrimeData {
   List<CrimeDetailsDTO> getDetailsWithCoordinates(List<CrimeDetails> crimeDetailsList);
}
