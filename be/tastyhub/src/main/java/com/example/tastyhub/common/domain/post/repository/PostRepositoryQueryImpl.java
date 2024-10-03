package com.example.tastyhub.common.domain.post.repository;

import static com.example.tastyhub.common.domain.post.entity.QPost.post;
import static com.example.tastyhub.common.domain.recipe.entity.QRecipe.recipe;
import static com.example.tastyhub.common.domain.user.entity.QUser.user;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.post.entity.Post;
import com.example.tastyhub.common.domain.village.entity.Village;
import com.example.tastyhub.common.utils.page.RestPage;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public RestPage<PagingPostResponse> findAllPostResponse(Village myVillage, Pageable pageable) {
    List<PagingPostResponse> pagingPostResponse = jpaQueryFactory.select(
            Projections.constructor(PagingPostResponse.class,
                post.id,
                post.title,
                post.postState,
                post.user.nickname,
                post.user.userImg
            ))
        .from(post)
        .leftJoin(post.user, user)
//            .leftJoin(post.village, village)
        .where(post.village.addressTownName.eq(myVillage.getAddressTownName()))
        .orderBy(getOrderSpecifiers(pageable)) // Dynamic sorting
        .offset(pageable.getOffset()) // Set offset for paging
        .limit(pageable.getPageSize()) // Set page size
        .fetch();
    long totalSize = countQuery().fetchOne();
    return new RestPage<>(pagingPostResponse, pageable, totalSize); // 직접 RestPage 반환
  }

  @Override
  public RestPage<PagingPostResponse> findAllRecentPostResponse(Village village, Pageable pageable) {

    List<PagingPostResponse> pagingPostResponse = jpaQueryFactory.select(
            Projections.constructor(PagingPostResponse.class,
                post.id,
                post.title,
                post.postState,
                post.user.nickname,
                post.user.userImg
            ))
        .from(post)
        .leftJoin(post.user)
        .where(post.village.addressTownName.eq(village.getAddressTownName()))
        .limit(10)
        .orderBy(post.createdAt.desc())
        .fetch();
    long totalSize = countQuery().fetchOne();
    return new RestPage<>(pagingPostResponse, pageable, totalSize); // 직접 RestPage 반환
  }

  @Override
  public Optional<PostResponse> findByIdQuery(Long postId) {
//
//    return Optional.ofNullable(jpaQueryFactory.select(
//            new QPostResponse(post.id, post.title, post.postState, post.user.nickname,
//                post.user.userImg, post.content,
//                post.modifiedAt, post.comments)).from(post)
//        .where(post.id.eq(postId))
//        .leftJoin(post.user).fetchOne());
        return null;
  }

  // Helper method to dynamically add sort specifiers with direction
  private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
    return pageable.getSort().stream()
        .map(order -> {
          PathBuilder pathBuilder = new PathBuilder<>(Post.class, "post");
          // Determine sorting direction (ascending or descending)
          return new OrderSpecifier<>(
              order.isAscending() ? Order.ASC : Order.DESC,
              pathBuilder.get(order.getProperty())); // Use the property name for sorting
        })
        .toArray(OrderSpecifier[]::new);
  }

  private JPAQuery<Long> countQuery() {
    return jpaQueryFactory.select(Wildcard.count)
        .from(recipe);
  }
}
