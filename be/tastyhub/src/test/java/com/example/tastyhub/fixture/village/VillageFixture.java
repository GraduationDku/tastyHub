package com.example.tastyhub.fixture.village;

import com.example.tastyhub.common.domain.village.dtos.LocationRequest;

public class VillageFixture {

    public static LocationRequest LOCATION_REQUEST = LocationRequest
        .builder()
        .lat((long) 37.395628)
        .lng((long) 126.929901)
        .addressTownName("탄현동")
        .build();

}
