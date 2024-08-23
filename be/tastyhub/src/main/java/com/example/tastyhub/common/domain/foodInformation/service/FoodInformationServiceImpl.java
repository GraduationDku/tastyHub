package com.example.tastyhub.common.domain.foodInformation.service;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FoodInformationServiceImpl implements FoodInformationService {

  @Override
  public FoodInformation createFoodInformation(RecipeCreateDto recipeCreateDto) {
    return FoodInformation.createFoodInformation(recipeCreateDto);
  }

  @Override
  public FoodInformationDto getFoodInformationDto(Recipe recipe) {
    return FoodInformationDto.getFoodInformationDto(recipe);
  }


  @Override
  public void relationRecipe(FoodInformation foodInformation, Recipe recipe) {
    foodInformation.updateRelation(recipe);
  }

  @Override
  public FoodInformation updateFoodInformation(
      FoodInformationDto foodInformationDto, FoodInformation foodInformation) {
    foodInformation.update(foodInformationDto);
    return foodInformation;
  }

}
