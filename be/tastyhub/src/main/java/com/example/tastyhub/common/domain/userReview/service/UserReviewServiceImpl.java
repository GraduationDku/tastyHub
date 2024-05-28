package com.example.tastyhub.common.domain.userReview.service;

import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.userReview.repository.UserReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserReviewServiceImpl implements UserReviewService{
    private final UserReviewRepository userReviewRepository;
}
