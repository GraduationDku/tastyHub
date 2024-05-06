package com.example.tastyhub.common.domain.post.repository;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.village.entity.Village;
import java.util.List;

public interface PostRepositoryQuery {

    List<PagingPostResponse> findAllPostResponse(Village village);
    List<PagingPostResponse> findAllRecentPostResponse(Village village);

}
