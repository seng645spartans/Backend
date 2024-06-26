package com.crimeMap.Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.crimeMap.Backend.Entities.University;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    // Find a university by its name
    University findByName(String name);

}
