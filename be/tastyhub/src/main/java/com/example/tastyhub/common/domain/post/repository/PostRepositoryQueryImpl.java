package com.example.tastyhub.common.domain.post.repository;

import static com.example.tastyhub.common.domain.post.entity.QPost.post;
import static com.example.tastyhub.common.domain.user.entity.QUser.user;
import static com.example.tastyhub.common.domain.village.entity.QVillage.village;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.post.dtos.QPostResponse;
import com.example.tastyhub.common.domain.village.entity.Village;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PagingPostResponse> findAllPostResponse(Village myVillage) {
        return jpaQueryFactory.select(Projections.constructor(PagingPostResponse.class,
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
            .fetch();
//        return List.of();
    }

    @Override
    public List<PagingPostResponse> findAllRecentPostResponse(Village village) {

        return jpaQueryFactory.select(Projections.constructor(PagingPostResponse.class,
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
//        return List.of();
    }

    @Override
    public Optional<PostResponse> findByIdQuery(Long postId) {

        return Optional.ofNullable(jpaQueryFactory.select(
                new QPostResponse(post.id, post.title, post.postState, post.user.nickname,
                    post.user.userImg, post.content,
                    post.modifiedAt, post.comments)).from(post)
            .where(post.id.eq(postId))
            .leftJoin(post.user).fetchOne());
//        return null;
    }
}
