package com.crimeMap.Backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CrimeDetails {
    private String location;
    private Timestamp dateReported;
    private String caseId;
    private String crimeType;
    private Timestamp dateOccurred;
    private String statusDisposition;
    private Timestamp updated;
}

