package com.example.tastyhub.common.domain.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    private double lat;
    private double lng;
//    private String addressTownName;
    
}
