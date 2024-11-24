package com.example.tastyhub.fixture.like;


import com.example.tastyhub.common.domain.like.dtos.LikeCheckDto;
import com.example.tastyhub.common.domain.like.dtos.LikeCountRequest;

public class LikeFixture {

  public static LikeCountRequest LIKE_COUNT_REQUEST = new LikeCountRequest(0L);

  public static LikeCheckDto LIKE_CHECK_DTO = new LikeCheckDto(true);
}
