package com.example.tastyhub.common.domain.userReview.service;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewCreateRequest;

public interface UserReviewService {

    void createUserReview(Long userId, UserReviewCreateRequest userReviewcCreateRequest, User user);

}
