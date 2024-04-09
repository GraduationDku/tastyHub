package com.example.tastyhub.common.domain.village.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationRequest {
    private long lat;
    private long lng;
    private String addressTownName;
    
}
