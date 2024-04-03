package com.crimeMap.Backend.Repository;

import com.crimeMap.Backend.Entities.CrimeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrimeDetailsRepository extends JpaRepository<CrimeDetails, Long> {

}

