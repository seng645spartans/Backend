package com.crimeMap.Backend.Services.CrimeTypeService;

import com.crimeMap.Backend.DTO.Response.CrimeTypeDTO;
import com.crimeMap.Backend.Entities.CrimeType;
import com.crimeMap.Backend.Repository.CrimeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrimeTypeServiceImpl implements CrimeTypeService{

    @Autowired
    private CrimeTypeRepository crimeTypeRepository;

    @Override
    public void updateCrimeTypes(List<CrimeTypeDTO> crimeTypeDTOs) {
        crimeTypeDTOs.forEach(dto -> {
            CrimeType crimeType = crimeTypeRepository.findByDescription(dto.getDescription());
            System.out.println("Crime Type : " +crimeType.toString());
            crimeType.setIsActive(dto.is_active());
            if(dto.is_active()){
                System.out.println("Crime Type is true");
                crimeType.setIsActive(true);
            }
            crimeTypeRepository.save(crimeType);
        });
    }

    @Override
    public List<CrimeTypeDTO> findAllCrimeTypes() {
        return crimeTypeRepository.findAllCrimesWithActiveStatus();
    }
}
