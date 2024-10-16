package com.example.tastyhub.common.domain.village.service;

import com.example.tastyhub.common.domain.naver.service.NaverService;
import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.village.dtos.LocationRequest;
import com.example.tastyhub.common.domain.village.entity.Village;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VillageServiceImpl implements VillageService {

//  private final VillageRepository villageRepository;

  private final NaverService naverService;

  @Override
  public Village getVillage(LocationRequest locationRequest) {
    double lat = locationRequest.getLat();
    double lng = locationRequest.getLng();

    String addressFromCoordinates;
    addressFromCoordinates = naverService.getAddressFromCoordinates(lat, lng);

    return Village.createVillage(
        addressFromCoordinates, lat, lng);
  }


  @Override
  public Village modifyLocation(LocationRequest locationRequest, User user) {

    Village village = user.getVillage();

    double lat = locationRequest.getLat();
    double lng = locationRequest.getLng();

    String addressFromCoordinates;
    addressFromCoordinates = naverService.getAddressFromCoordinates(lat, lng);

    village.update(locationRequest, addressFromCoordinates);

    return village;
  }

//  @Generated
//  private Village findByUserId(User user) {
//    return villageRepository.findByUserId(user.getId())
//        .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지않습니다"));
//  }


}
