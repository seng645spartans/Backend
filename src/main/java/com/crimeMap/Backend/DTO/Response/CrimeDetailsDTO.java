package com.crimeMap.Backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class CrimeDetailsDTO {
    private String location;
    private Timestamp dateReported;
    private String caseId;
    private String crimeType;
    private Timestamp dateOccurred;
    private String statusDisposition;
    private Timestamp updated;
    private Double latitude;
    private Double longitude;

    public CrimeDetailsDTO() {

    }
}
