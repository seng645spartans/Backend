package com.crimeMap.Backend.Repository;

import com.crimeMap.Backend.DTO.Response.CrimeTypeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.crimeMap.Backend.Entities.CrimeType;

import java.util.List;

@Repository
public interface CrimeTypeRepository extends JpaRepository<CrimeType, Long> {
    // Find a crime type by its description
    CrimeType findByDescription(String description);

    @Query("SELECT c FROM CrimeType c WHERE LOWER(c.description) = LOWER(?1)")
    CrimeType findByDescriptionIgnoreCase(String description);

    @Query("SELECT c.description FROM CrimeType c")
    List<String> findAllDescriptions();

    @Query("SELECT c.description FROM CrimeType c WHERE c.isActive = true")
    List<String> findActiveCrimeDescriptions();

    @Query("SELECT new com.crimeMap.Backend.DTO.Response.CrimeTypeDTO(c.id,c.description, c.isActive) FROM CrimeType c")
    List<CrimeTypeDTO> findAllCrimesWithActiveStatus();
}
