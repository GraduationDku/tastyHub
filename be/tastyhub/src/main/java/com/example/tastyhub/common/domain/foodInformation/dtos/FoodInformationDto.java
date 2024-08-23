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

    private String text;

    private Long cookingTime;

    private String serving;

    public static FoodInformationDto getFoodInformationDto(Recipe recipe) {
        return FoodInformationDto.builder()
            .foodInformationId(recipe.getFoodInformation().getId())
            .text(recipe.getFoodInformation().getText())
            .cookingTime(recipe.getFoodInformation().getCookingTime())
            .serving(recipe.getFoodInformation().getServing())
            .build();
    }

}
