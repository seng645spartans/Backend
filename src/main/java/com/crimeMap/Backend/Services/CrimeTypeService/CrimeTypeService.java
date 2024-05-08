package com.crimeMap.Backend.Services.CrimeTypeService;

import com.crimeMap.Backend.DTO.Response.CrimeTypeDTO;

import java.util.List;

public interface CrimeTypeService {

    void updateCrimeTypes(List<CrimeTypeDTO> crimeTypes);

    public List<CrimeTypeDTO> findAllCrimeTypes();
}
