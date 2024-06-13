package com.example.tastyhub.common.domain.village.controller;

import static com.example.tastyhub.common.config.APIConfig.VILLAGE_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.village.dtos.LocationRequest;
import com.example.tastyhub.common.domain.village.service.VillageService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(VILLAGE_API)
@RequiredArgsConstructor
public class VillageController {

    private final VillageService villageService;

    @PostMapping("/location")
    public ResponseEntity<StatusResponse> setLocation(@RequestBody LocationRequest locationRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        villageService.setLocation(locationRequest, userDetails.getUser());
        return RESPONSE_OK;
    }
    @PatchMapping("/modify/location")
    public ResponseEntity<StatusResponse> modifyLocation(@RequestBody LocationRequest locationRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        villageService.modifyLocation(locationRequest, userDetails.getUser());
        return RESPONSE_OK;
    }

}
