package com.crimeMap.Backend.Repository;

import com.crimeMap.Backend.Entities.CrimeDetails;
import com.crimeMap.Backend.Entities.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CrimeDetailsRepository extends JpaRepository<CrimeDetails, Long> {
    List<CrimeDetails> findByUniversityAndDateReportedAfter(University university, Timestamp date);

    @Query("SELECT cd FROM CrimeDetails cd WHERE cd.university = :university " +
            "AND cd.dateReported > :date " +
            "AND cd.crimeTypeID.isActive = TRUE")
    List<CrimeDetails> findByUniversityAndDateReportedAfterAndCrimeTypeActive(
            @Param("university") University university,
            @Param("date") Timestamp date);


    CrimeDetails getByCaseId(String caseId);

    List<CrimeDetails> findByCreatedAtAfterAndUniversityId(Timestamp createdAt, Long universityId);
}


