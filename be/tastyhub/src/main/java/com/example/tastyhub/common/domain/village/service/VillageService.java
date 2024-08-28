package com.example.tastyhub.common.domain.village.service;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.village.dtos.LocationRequest;
import com.example.tastyhub.common.domain.village.entity.Village;

public interface VillageService {


    Village setLocation(LocationRequest locationRequest, User user);

    Village modifyLocation(LocationRequest locationRequest, User user);

}
