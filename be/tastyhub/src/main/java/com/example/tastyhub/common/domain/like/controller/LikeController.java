package com.example.tastyhub.common.domain.like.controller;

import static com.example.tastyhub.common.config.APIConfig.LIKE_API;

import com.example.tastyhub.common.domain.like.dtos.LikeCheckDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.like.dtos.LikeCountRequest;
import com.example.tastyhub.common.domain.like.service.LikeService;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping(LIKE_API)
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final SetHttpHeaders setHttpHeaders;

    @PostMapping("/{recipeId}")
    public ResponseEntity<LikeCheckDto> like(@PathVariable Long recipeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LikeCheckDto like = likeService.like(recipeId, userDetails.getUser());

        return ResponseEntity.ok().headers(setHttpHeaders.setHttpHeaderTypeJson()).body(like);
    }

    @GetMapping("/count/{recipeId}")
    public ResponseEntity<LikeCountRequest> count(@PathVariable Long recipeId){
        LikeCountRequest likeCountRequest = likeService.count(recipeId);
        
        return ResponseEntity.ok().headers(setHttpHeaders.setHttpHeaderTypeJson()).body(likeCountRequest);
    }
    
}
