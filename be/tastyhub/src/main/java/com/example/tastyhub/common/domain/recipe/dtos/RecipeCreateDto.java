package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateRequest;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCreateDto {

    private String foodName;

    private String foodImg;

    private FoodInformationCreateDto foodInformation;

    private List<IngredientCreateDto> ingredients;

    private List<CookStepCreateRequest> cookSteps;


}
