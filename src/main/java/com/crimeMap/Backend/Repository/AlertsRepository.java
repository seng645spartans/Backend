package com.crimeMap.Backend.Repository;

import com.crimeMap.Backend.Entities.Alerts;
import com.crimeMap.Backend.Entities.CrimeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Long> {

}