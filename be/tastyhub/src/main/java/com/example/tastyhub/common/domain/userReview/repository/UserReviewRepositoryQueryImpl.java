package com.example.tastyhub.common.domain.userReview.repository;

import java.util.List;

import static com.example.tastyhub.common.domain.userReview.entity.QUserReview.userReview;
import static com.example.tastyhub.common.domain.user.entity.QUser.user;

import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserReviewRepositoryQueryImpl implements UserReviewRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PagingUserReviewResponse> findAllUserReviewResponse(Long userId) {
        List<PagingUserReviewResponse> pagingUserReviewResponseList =
             jpaQueryFactory.select(Projections.constructor(PagingUserReviewResponse.class,
                    user.id,
                    user.nickname,
                    userReview.grade,
                    userReview.text
                    ))
                    .from(userReview)
                    .where(userReview.reader.id.eq(userId))
                    .leftJoin(userReview.writer,user)
                    .fetch();
        return pagingUserReviewResponseList;
    }
    
}
