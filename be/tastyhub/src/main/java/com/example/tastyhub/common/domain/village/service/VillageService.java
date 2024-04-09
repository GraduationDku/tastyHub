package com.example.tastyhub.common.domain.village.service;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.village.dtos.LocationRequest;

public interface VillageService {


    void location(LocationRequest locationRequest, User user);

}
