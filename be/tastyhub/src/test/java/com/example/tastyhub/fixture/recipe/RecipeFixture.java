package com.example.tastyhub.fixture.recipe;

import static com.example.tastyhub.fixture.cookStep.CookStepFixture.COOK_STEP;
import static com.example.tastyhub.fixture.cookStep.CookStepFixture.COOK_STEP_CREATE_DTO;
import static com.example.tastyhub.fixture.cookStep.CookStepFixture.COOK_STEP_RESPONSE_DTO;
import static com.example.tastyhub.fixture.cookStep.CookStepFixture.COOK_STEP_UPDATE_DTO_LIST;
import static com.example.tastyhub.fixture.foodInformation.FoodInformationFixture.FOOD_INFORMATION;
import static com.example.tastyhub.fixture.foodInformation.FoodInformationFixture.FOOD_INFORMATION_CREATE_DTO;
import static com.example.tastyhub.fixture.foodInformation.FoodInformationFixture.FOOD_INFORMATION_DTO;
import static com.example.tastyhub.fixture.ingredient.IngredientFixture.INGREDIENT;
import static com.example.tastyhub.fixture.ingredient.IngredientFixture.INGREDIENT_CREATE_DTO;
import static com.example.tastyhub.fixture.ingredient.IngredientFixture.INGREDIENT_DTO;
import static com.example.tastyhub.fixture.ingredient.IngredientFixture.INGREDIENT_DTO_LIST;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepUpdateDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import java.util.Collection;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    public static final RecipeCreateDto RECIPE_CREATE_DTO = RecipeCreateDto.builder()
        .foodName("name")
        .foodImg(null)
        .foodInformation(FOOD_INFORMATION_CREATE_DTO)
        .cookSteps(Collections.singletonList(COOK_STEP_CREATE_DTO))
        .ingredients(Collections.singletonList(INGREDIENT_CREATE_DTO))
        .build();

    public static final RecipeUpdateDto RECIPE_UPDATE_DTO = RecipeUpdateDto
        .builder()
        .foodName("foodname")
        .foodInformation(FOOD_INFORMATION_DTO)
        .foodImg(RECIPE_CREATE_DTO.getFoodImg())
        .cookSteps(COOK_STEP_UPDATE_DTO_LIST)
        .ingredients(INGREDIENT_DTO_LIST)
        .build();


    public static final PagingRecipeResponse PAGING_RECIPE_RESPONSE = PagingRecipeResponse.builder()
        .foodName("foodname")
        .foodInformationDto(FOOD_INFORMATION_DTO)
        .foodImgUrl("ImgURL")
        .foodId(1L)
        .build();

    public static final Page<PagingRecipeResponse> PAGING_RECIPE_RESPONSE_PAGE = new PageImpl<>(
        Collections.singletonList(PAGING_RECIPE_RESPONSE));

}
