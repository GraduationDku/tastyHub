package com.example.tastyhub.common.domain.ingredient.dtos;

import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngredientDto {

    private Long ingredientId;
    private String ingredientName;
    private String amount;

    public IngredientDto(Ingredient ingredient) {
        this.ingredientId = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
        this.amount = ingredient.getAmount();
    }
}
