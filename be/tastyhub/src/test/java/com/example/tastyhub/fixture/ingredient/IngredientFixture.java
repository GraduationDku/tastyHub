package com.example.tastyhub.fixture.ingredient;

import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;
import java.util.Collections;
import java.util.List;

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

    public static final IngredientCreateDto INGREDIENT_CREATE_DTO = IngredientCreateDto.builder()
        .ingredientName("name")
        .amount("1")
        .build();


    public static final List<IngredientDto> INGREDIENT_DTO_LIST = Collections.singletonList(
        INGREDIENT_DTO);
}
