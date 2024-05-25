package com.example.tastyhub.common.domain.scrap.dtos;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class PagingScrapResponse {
    private Long foodId;
    private String foodName;
    private String foodImgUrl;
    @QueryProjection
    public PagingScrapResponse(Long foodId, String foodName, String foodImgUrl) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImgUrl = foodImgUrl;
    }
}
