package com.example.tastyhub.common.domain.post.repository;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.village.entity.Village;
import java.util.List;
import java.util.Optional;

public interface PostRepositoryQuery {

    List<PagingPostResponse> findAllPostResponse(Village village);
    List<PagingPostResponse> findAllRecentPostResponse(Village village);

    Optional<PostResponse> findByIdQuery(Long postId);
}
