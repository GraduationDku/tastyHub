package com.example.tastyhub.common.domain.userReview.repository;

import com.querydsl.core.types.OrderSpecifier;
import java.util.List;

import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserReviewRepositoryQuery {
    Page<PagingUserReviewResponse> findAllUserReviewResponse(Long userId, Pageable pageable, OrderSpecifier<?>[] orderSpecifiers);
}
