package com.example.tastyhub.common.domain.userReview.service;

import java.util.List;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewCreateRequest;

public interface UserReviewService {

    void createUserReview(Long userId, UserReviewCreateRequest userReviewcCreateRequest, User user);

    List<PagingUserReviewResponse> getUserReviews(Long userId);

}
