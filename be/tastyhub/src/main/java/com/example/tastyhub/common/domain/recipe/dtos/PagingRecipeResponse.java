package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class PagingRecipeResponse {
    private Long foodId;
    private String foodName;
    private String foodImgUrl;

    private FoodInformationDto foodInformationDto;

    @QueryProjection
    public PagingRecipeResponse(Long foodId, String foodName, String foodImgUrl, Long foodInfoId, String foodInfoText,
            Long foodInfoCookongtime, String foodInfoServing) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImgUrl = foodImgUrl;
        this.foodInformationDto = FoodInformationDto.builder().foodInformationId(foodInfoId).text(foodInfoText)
                .cookingTime(foodInfoCookongtime).serving(foodInfoServing).build();
    }
}
