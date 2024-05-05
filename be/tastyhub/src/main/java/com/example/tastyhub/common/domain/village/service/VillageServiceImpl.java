package com.example.tastyhub.common.domain.village.service;

import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.village.dtos.LocationRequest;
import com.example.tastyhub.common.domain.village.entity.Village;
import com.example.tastyhub.common.domain.village.repository.VillageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VillageServiceImpl implements VillageService{

    private final VillageRepository villageRepository;

    @Override
    @Transactional
    public void
    setLocation(LocationRequest locationRequest, User user) {
        String addressTownName = locationRequest.getAddressTownName();
        long lat = locationRequest.getLat();
        long lng = locationRequest.getLng();
        

        Village village = Village.builder()
                .addressTownName(addressTownName)
                .lat(lat)
                .lng(lng)
                .user(user)
                .build();

        villageRepository.save(village);
    }
    @Override
    public void modifyLocation(LocationRequest locationRequest, User user) {

        Village village = findByUserId(user);

        village.update(locationRequest);

        villageRepository.save(village);
    }

    private Village findByUserId(User user) {
        return villageRepository.findByUserId(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지않습니다"));
    }


}
