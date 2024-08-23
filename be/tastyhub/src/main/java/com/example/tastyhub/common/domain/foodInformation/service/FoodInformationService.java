package com.example.tastyhub.common.domain.foodInformation.service;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;

public interface FoodInformationService {

  FoodInformation createFoodInformation(RecipeCreateDto recipeCreateDto);

  FoodInformationDto getFoodInformationDto(Recipe recipe);

  void relationRecipe(FoodInformation foodInformation, Recipe recipe);

  FoodInformation updateFoodInformation(FoodInformationDto recipeUpdateDto, FoodInformation recipe);
}
