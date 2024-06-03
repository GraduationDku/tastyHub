package com.example.tastyhub.common.domain.post.repository;

import static com.example.tastyhub.common.domain.post.entity.QPost.post;

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
    public List<PagingPostResponse> findAllPostResponse(Village village) {
        List<PagingPostResponse> postResponseList =
            jpaQueryFactory.select(Projections.constructor(PagingPostResponse.class,
                    post.id,
                    post.title,
                    post.postState,
                    post.user.nickname,
                    post.user.userImg
                ))
                .from(post)
                .where(post.user.village.addressTownName.eq(village.getAddressTownName()))
                .leftJoin(post.user).fetchJoin()
                .leftJoin(post.user.village).fetchJoin()
                .fetch();
        return postResponseList;
    }

    @Override
    public List<PagingPostResponse> findAllRecentPostResponse(Village village) {
        List<PagingPostResponse> postResponseList =
            jpaQueryFactory.select(Projections.constructor(PagingPostResponse.class,
                    post.id,
                    post.title,
                    post.postState,
                    post.user.nickname,
                    post.user.userImg
                ))
                .from(post)
                .where(post.user.village.addressTownName.eq(village.getAddressTownName()))
                .leftJoin(post.user).fetchJoin()
                .leftJoin(post.user.village).fetchJoin()
                .limit(10)
                .orderBy(post.createdAt.desc())
                .fetch();
        return postResponseList;
    }

    @Override
    public Optional<PostResponse> findByIdQuery(Long postId) {
        Optional<PostResponse> postResponse = Optional.ofNullable(jpaQueryFactory.select(
                new QPostResponse(post.id, post.title, post.postState, post.user.nickname,
                    post.user.userImg, post.text,
                    post.modifiedAt, post.comments)).from(post)
            .where(post.id.eq(postId))
            .leftJoin(post.user).fetchOne());

        return postResponse;
    }
}
