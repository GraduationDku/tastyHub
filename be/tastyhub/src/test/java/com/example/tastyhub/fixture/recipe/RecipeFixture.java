package com.example.tastyhub.fixture.recipe;

import static com.example.tastyhub.fixture.cookStep.CookStepFixture.COOK_STEP;
import static com.example.tastyhub.fixture.cookStep.CookStepFixture.COOK_STEP_RESPONSE_DTO;
import static com.example.tastyhub.fixture.foodInformation.FoodInformationFixture.FOOD_INFORMATION;
import static com.example.tastyhub.fixture.foodInformation.FoodInformationFixture.FOOD_INFORMATION_DTO;
import static com.example.tastyhub.fixture.ingredient.IngredientFixture.INGREDIENT;
import static com.example.tastyhub.fixture.ingredient.IngredientFixture.INGREDIENT_DTO;

import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import java.util.Collection;
import java.util.Collections;

public class RecipeFixture {

    public static final Recipe RECIPE = Recipe.builder()
        .id(1L)
        .foodName("foodname")
        .foodImgUrl("foodImage")
        .foodInformation(FOOD_INFORMATION)
        .ingredients(Collections.singletonList(INGREDIENT))
        .cookSteps(Collections.singletonList(COOK_STEP))
        .build();

    public static final RecipeDto RECIPE_DTO = RecipeDto.builder()
        .foodId(1L)
        .foodName("name")
        .foodImgUrl("url")
        .foodInformation(FOOD_INFORMATION_DTO)
        .cookSteps(Collections.singletonList(COOK_STEP_RESPONSE_DTO))
        .ingredients(Collections.singletonList(INGREDIENT_DTO))
        .build();
    


}
