package com.example.tastyhub.common.domain.scrap.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.tastyhub.common.domain.scrap.dtos.PagingScrapResponse;
import com.example.tastyhub.common.domain.user.entity.User;

public interface ScrapService {

    boolean scrap(Long recipeId, User user);

    Page<PagingScrapResponse> getScrapList(User user, Pageable pageable);


}
