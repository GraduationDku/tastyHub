package com.example.tastyhub.common.domain.ingredient.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngredientCreateDto {

    private String ingredientName;
    private String amount;

}
