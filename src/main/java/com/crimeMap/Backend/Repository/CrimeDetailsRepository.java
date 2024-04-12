package com.crimeMap.Backend.Repository;

import com.crimeMap.Backend.Entities.CrimeDetails;
import com.crimeMap.Backend.Entities.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CrimeDetailsRepository extends JpaRepository<CrimeDetails, Long> {
    List<CrimeDetails> findByUniversityAndDateReportedAfter(University university, Timestamp date);

    CrimeDetails getByCaseId(String caseId);

    List<CrimeDetails> findByCreatedAtAfterAndUniversityId(Timestamp createdAt, Long universityId);
}


