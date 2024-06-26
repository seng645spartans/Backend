package com.crimeMap.Backend.Services.CrimeDataExtraction;

import com.crimeMap.Backend.Entities.CrimeDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


public interface CrimeDetailsExtraction {

    List<CrimeDetails> getCrimeDetails(String universityName) throws IOException;
}
