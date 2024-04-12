package com.crimeMap.Backend.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrimeDetailsInfoDTO {
    private String type;
    private String date;
    private String description;
    private String address;
}
