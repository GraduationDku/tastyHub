package com.example.tastyhub.common.domain.like.service;

import com.example.tastyhub.common.domain.user.entity.User;

public interface LikeService {

    boolean like(Long recipeId, User user);

}
