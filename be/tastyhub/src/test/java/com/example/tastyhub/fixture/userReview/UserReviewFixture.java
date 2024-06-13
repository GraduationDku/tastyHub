package com.example.tastyhub.fixture.userReview;

import static com.example.tastyhub.fixture.user.UserFixture.USER;

import java.util.Collections;
import java.util.List;

import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewCreateRequest;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewUpdateRequest;
import com.example.tastyhub.common.domain.userReview.entity.UserReview;

public class UserReviewFixture {
    public static final UserReview USER_REVIEW =
        UserReview.builder()
            .id(1L)
            .reader(USER)
            .writer(USER)
            .grade(0)
            .text("text")
            .build();
    public static final UserReviewCreateRequest USER_REVIEW_CREATE_REQUEST =
        UserReviewCreateRequest.builder()
            .grade(0L)
            .text("text")
            .build();
    public static final UserReviewUpdateRequest USER_REVIEW_UPDATE_REQUEST =
            UserReviewUpdateRequest.builder()
                .grade(1L)
                .text("new_text")
                .build();
    public static final PagingUserReviewResponse PAGING_USER_REVIEW_RESPONSE = PagingUserReviewResponse.builder()
        .userId(USER.getId())
        .grade(0L)
        .nickname(USER.getNickname())
        .text("text")
        .build();
    public static final List<PagingUserReviewResponse> PAGING_USER_REVIEW_RESPONSES = Collections.singletonList(
        PAGING_USER_REVIEW_RESPONSE);
}
