package com.example.tastyhub.common.domain.userReview.controller;

import static com.example.tastyhub.common.config.APIConfig.USERREVIEW_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.userReview.service.UserReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(USERREVIEW_API)
@RequiredArgsConstructor
public class UserReviewController {
    private final UserReviewService userReviewService;
}
