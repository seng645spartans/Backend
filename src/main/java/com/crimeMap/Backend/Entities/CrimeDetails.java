package com.crimeMap.Backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "crime_details")
public class CrimeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @JoinColumn(name = "crime_type_id")
    private CrimeType crimeTypeID;

    @Column(name = "location")
    private String location;

    @Column(name = "date_reported")
    private Timestamp dateReported;

    @Column(name = "case_id")
    private String caseId;

    @Column(name = "crime_type")
    private String crimeType;

    @Column(name = "details")
    private String details;

    @Column(name = "date_occurred")
    private Timestamp dateOccurred;

    @Column(name = "status_disposition")
    private String statusDisposition;

    @Column(name = "updated")
    private Timestamp updated;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}

