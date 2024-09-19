package com.example.tastyhub.common.domain.userReview.service;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserReviewService {

    void createUserReview(Long userId, UserReviewRequest userReviewRequest, User user);

    Page<PagingUserReviewResponse> getUserReviews(Long userId, Pageable pageable);

    void updateUserReviewByUserReviewUpdateRequest(Long userReviewId, UserReviewRequest userReviewUpdateRequest, User user);

    void deleteUserReview(Long userReviewId, User user);

}
