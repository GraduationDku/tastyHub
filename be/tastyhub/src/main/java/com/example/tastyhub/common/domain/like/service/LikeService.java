package com.example.tastyhub.common.domain.like.service;

import com.example.tastyhub.common.domain.like.dtos.LikeCheckDto;
import com.example.tastyhub.common.domain.like.dtos.LikeCountRequest;
import com.example.tastyhub.common.domain.user.entity.User;

public interface LikeService {

    LikeCheckDto like(Long recipeId, User user);

    LikeCountRequest count(Long recipeId);

}
