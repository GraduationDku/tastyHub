package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingRecipeResponse {
    private Long foodId;
    private String foodName;
    private String foodImgUrl;

    private FoodInformationDto foodInformationDto;
}
