package com.example.tastyhub.common.domain.userReview.repository;

import java.util.List;

import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;

public interface UserReviewRepositoryQuery {
    List<PagingUserReviewResponse> findAllUserReviewResponse(Long userId);
}
