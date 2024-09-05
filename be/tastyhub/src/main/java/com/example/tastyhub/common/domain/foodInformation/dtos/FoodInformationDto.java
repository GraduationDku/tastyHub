package com.example.tastyhub.common.domain.foodInformation.dtos;

import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodInformationDto {

    private Long foodInformationId;

    private String content;

    private Long cookingTime;

    private String serving;

    public static FoodInformationDto getFoodInformationDto(Recipe recipe) {
        return FoodInformationDto.builder()
            .foodInformationId(recipe.getFoodInformation().getId())
            .content(recipe.getFoodInformation().getContent())
            .cookingTime(recipe.getFoodInformation().getCookingTime())
            .serving(recipe.getFoodInformation().getServing())
            .build();
    }

}
