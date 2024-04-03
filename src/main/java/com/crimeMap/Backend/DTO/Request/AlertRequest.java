package com.crimeMap.Backend.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlertRequest {
    private String university;
    private String email;
    private Map<String, Boolean> crimes;
}
