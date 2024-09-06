package com.example.tastyhub.common.domain.userReview.service;

import java.util.List;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewCreateRequest;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserReviewService {

    void createUserReview(Long userId, UserReviewCreateRequest userReviewCreateRequest, User user);

    Page<PagingUserReviewResponse> getUserReviews(Long userId, Pageable pageable);

    void updateUserReviewByUserReviewUpdateRequest(Long userReviewId, UserReviewUpdateRequest userReviewUpdateRequest, User user);

    void deleteUserReview(Long userReviewId, User user);

}
