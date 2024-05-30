package com.example.tastyhub.common.domain.userReview.controller;

import com.example.tastyhub.common.domain.userReview.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserReviewController {

    private UserReviewService userReviewService;
}
