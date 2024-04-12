package com.crimeMap.Backend.Repository;

import com.crimeMap.Backend.Entities.Alerts;
import com.crimeMap.Backend.Entities.AlertsScheduler;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;


public interface AlertSchedulerRepository extends JpaRepository<AlertsScheduler, Long> {
    AlertsScheduler findTopByOrderByLastSchedulerRunDesc();

    @Transactional
    @Modifying
    @Query("update AlertsScheduler a set a.lastSchedulerRun = ?1")
    void updateLastSchedulerRun(Timestamp lastRun);
}
