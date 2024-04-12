package com.crimeMap.Backend.Repository;

import com.crimeMap.Backend.Entities.Alerts;
import com.crimeMap.Backend.Entities.CrimeDetailsScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrimeDetailsSchedulerRepository extends JpaRepository<com.crimeMap.Backend.Entities.CrimeDetailsScheduler, Long> {

    Optional<CrimeDetailsScheduler> findFirstByUniversityOrderByLastScheduledRunDesc(String universityName);
}
