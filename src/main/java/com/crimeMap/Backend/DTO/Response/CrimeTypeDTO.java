package com.crimeMap.Backend.DTO.Response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrimeTypeDTO {
    private Long id;
    private String description;
    @JsonProperty("isActive")
    private boolean is_active;
}
