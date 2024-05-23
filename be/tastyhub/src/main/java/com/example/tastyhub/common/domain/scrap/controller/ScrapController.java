package com.example.tastyhub.common.domain.scrap.controller;

import static com.example.tastyhub.common.config.APIConfig.SCRAP_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.scrap.service.ScrapService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(SCRAP_API)
@RequiredArgsConstructor
public class ScrapController {
    private final ScrapService scrapService;

    @PostMapping("/{recipeId}")
    public ResponseEntity<StatusResponse> postMethodName(@PathVariable Long recipeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scrapService.scrap(recipeId, userDetails.getUser());
        return RESPONSE_OK;
    }

}
