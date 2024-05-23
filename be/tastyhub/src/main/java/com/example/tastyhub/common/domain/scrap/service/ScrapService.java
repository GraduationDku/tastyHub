package com.example.tastyhub.common.domain.scrap.service;

import com.example.tastyhub.common.domain.user.entity.User;

public interface ScrapService {

    boolean scrap(Long recipeId, User user);


}
