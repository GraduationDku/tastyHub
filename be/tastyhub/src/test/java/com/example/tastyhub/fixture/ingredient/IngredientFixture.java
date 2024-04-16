package com.example.tastyhub.fixture.ingredient;

import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;

public class IngredientFixture {

    public static final Ingredient INGREDIENT = Ingredient.builder()
        .id(1L)
        .ingredientName("name")
        .amount("1")
        .build();

    public static final IngredientDto INGREDIENT_DTO = IngredientDto.builder()
        .ingredientId(1L)
        .ingredientName("name")
        .amount("1")
        .build();


}
