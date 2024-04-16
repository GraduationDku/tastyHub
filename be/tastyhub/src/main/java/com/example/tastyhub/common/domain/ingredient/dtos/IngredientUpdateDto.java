package com.example.tastyhub.common.domain.ingredient.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngredientUpdateDto {

    private Long ingredientId;
    private String ingredientName;
    private String amount;
}
