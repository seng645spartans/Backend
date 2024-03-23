package com.crimeMap.Backend.Services.CrimeDataExtraction;

import com.crimeMap.Backend.Entities.CrimeDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("UMCP")
public class CrimeDetailsExtarctionUMCPImpl implements CrimeDetailsExtraction{
    @Override
    public List<CrimeDetails> getCrimeDetails(String universityName) {
        return null;
    }
}
