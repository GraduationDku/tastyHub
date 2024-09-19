package com.example.tastyhub.fixture.userReview;

import static com.example.tastyhub.fixture.user.UserFixture.USER;

import com.example.tastyhub.common.utils.OrderSpecifierUtil;
import com.querydsl.core.types.OrderSpecifier;
import java.util.List;

import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewRequest;
import com.example.tastyhub.common.domain.userReview.entity.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class UserReviewFixture {
    public static final UserReview USER_REVIEW =
        UserReview.builder()
            .id(1L)
            .reader(USER)
            .writer(USER)
            .grade(0)
            .content("text")
            .build();
    public static final UserReviewRequest USER_REVIEW_CREATE_REQUEST =
        UserReviewRequest.builder()
            .grade(0L)
            .content("text")
            .build();
    public static final UserReviewRequest USER_REVIEW_UPDATE_REQUEST =
            UserReviewRequest.builder()
                .grade(1L)
                .content("new_text")
                .build();
    public static final PagingUserReviewResponse PAGING_USER_REVIEW_RESPONSE = PagingUserReviewResponse.builder()
        .grade(0L)
        .nickname(USER.getNickname())
        .content("text")
        .build();

    public static Pageable pageable = PageRequest.of(0, 1);

    public static final Page<PagingUserReviewResponse> PAGING_USER_REVIEW_RESPONSES = new PageImpl(List.of(PAGING_USER_REVIEW_RESPONSE),pageable, 1);

    public static OrderSpecifier<?>[] orderSpecifiers = OrderSpecifierUtil.getOrderSpecifiers(pageable, UserReview.class, "userReview");

}
