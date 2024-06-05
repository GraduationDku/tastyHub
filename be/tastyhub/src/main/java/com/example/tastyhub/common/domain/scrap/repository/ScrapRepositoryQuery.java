package com.example.tastyhub.common.domain.scrap.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.tastyhub.common.domain.scrap.dtos.PagingScrapResponse;

public interface ScrapRepositoryQuery {
    public Page<PagingScrapResponse> findScrapedRecipe(Pageable pageable, Long userId);
}
