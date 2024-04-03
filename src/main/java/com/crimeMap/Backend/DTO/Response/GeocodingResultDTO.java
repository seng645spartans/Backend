package com.crimeMap.Backend.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeocodingResultDTO {

    private Double lat;
    private Double lng;

}
