package com.example.tastyhub.common.domain.post.repository;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.village.entity.Village;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryQuery {

    Page<PagingPostResponse> findAllPostResponse(Village village, Pageable pageable);
    Page<PagingPostResponse> findAllRecentPostResponse(Village village, Pageable pageable);

    Optional<PostResponse> findByIdQuery(Long postId);
}
