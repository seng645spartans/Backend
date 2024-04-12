package com.crimeMap.Backend.Repository;

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
}
