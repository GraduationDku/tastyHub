package com.example.tastyhub.fixture.village;

import static com.example.tastyhub.fixture.user.UserFixture.USER;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.village.dtos.LocationRequest;
import com.example.tastyhub.common.domain.village.entity.Village;

public class VillageFixture {

    public static LocationRequest LOCATION_REQUEST = LocationRequest
        .builder()
        .lat((long) 37.395628)
        .lng((long) 126.929901)
        .addressTownName("탄현동")
        .build();

    public static Village VILLAGE = Village.builder()
        .addressTownName("동네이름")
        .user(USER)
        .lng(1L)
        .lat(1L)
        .build();

}
