package com.example.tastyhub.common.domain.userReview.repository;

import com.example.tastyhub.common.domain.post.entity.Post;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.List;

import static com.example.tastyhub.common.domain.userReview.entity.QUserReview.userReview;
import static com.example.tastyhub.common.domain.user.entity.QUser.user;

import com.example.tastyhub.common.domain.userReview.dtos.PagingUserReviewResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class UserReviewRepositoryQueryImpl implements UserReviewRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<PagingUserReviewResponse> findAllUserReviewResponse(Long userId, Pageable pageable,
      OrderSpecifier<?>[] orderSpecifiers) {
    List<PagingUserReviewResponse> pagingUserReviewResponseList =
        jpaQueryFactory.select(Projections.constructor(PagingUserReviewResponse.class,
                user.id,
                user.nickname,
                userReview.grade,
                userReview.content
            ))
            .from(userReview)
            .where(userReview.reader.id.eq(userId))
            .orderBy(orderSpecifiers) // Dynamic sorting
            .offset(pageable.getOffset()) // Set offset for paging
            .limit(pageable.getPageSize()) // Set page size
            .leftJoin(userReview.writer, user)
            .fetch();
    long totalSize = pagingUserReviewResponseList.size();
    return PageableExecutionUtils.getPage(pagingUserReviewResponseList, pageable, () -> totalSize);
  }


}
