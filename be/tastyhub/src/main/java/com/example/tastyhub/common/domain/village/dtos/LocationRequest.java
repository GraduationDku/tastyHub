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
    private long lat;
    private long lng;
    private String addressTownName;
    
}
