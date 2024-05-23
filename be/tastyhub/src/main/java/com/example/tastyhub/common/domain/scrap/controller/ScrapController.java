package com.example.tastyhub.common.domain.scrap.controller;

import static com.example.tastyhub.common.config.APIConfig.SCRAP_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.scrap.dtos.PagingScrapResponse;
import com.example.tastyhub.common.domain.scrap.service.ScrapService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(SCRAP_API)
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final SetHttpHeaders setHttpHeaders;

    @PostMapping("/{recipeId}")
    public ResponseEntity<StatusResponse> scrap(@PathVariable Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scrapService.scrap(recipeId, userDetails.getUser());
        return RESPONSE_OK;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PagingScrapResponse>> getScrapList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PageableDefault(size = 100, sort = "createdAt", direction = Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().headers(setHttpHeaders.setHttpHeaderTypeJson())
                    .body(scrapService.getScrapList(userDetails.getUser(),pageable));
    }

}
