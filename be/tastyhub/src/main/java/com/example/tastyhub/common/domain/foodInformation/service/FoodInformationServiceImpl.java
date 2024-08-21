package com.example.tastyhub.common.domain.foodInformation.service;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FoodInformationServiceImpl implements FoodInformationService {

  @Override
  public FoodInformation createFoodInformation(RecipeCreateDto recipeCreateDto) {
    return FoodInformation.builder()
        .text(recipeCreateDto.getFoodInformation().getText())
        .serving(recipeCreateDto.getFoodInformation().getServing())
        .cookingTime(recipeCreateDto.getFoodInformation().getCookingTime())
        .build();
  }

  @Override
  public FoodInformationDto getFoodInformationDto(Recipe recipe) {
    return FoodInformationDto.builder()
        .foodInformationId(recipe.getFoodInformation().getId())
        .text(recipe.getFoodInformation().getText())
        .cookingTime(recipe.getFoodInformation().getCookingTime())
        .serving(recipe.getFoodInformation().getServing())
        .build();
  }


  @Override
  public void relationRecipe(FoodInformation foodInformation, Recipe recipe) {
    foodInformation.updateRelation(recipe);
  }

  @Override
  public FoodInformation updateFoodInformation(RecipeUpdateDto recipeUpdateDto, Recipe recipe) {
    FoodInformationDto foodInformationDto = recipeUpdateDto.getFoodInformation();

    FoodInformation foodInformation = recipe.getFoodInformation();
    foodInformation.update(foodInformationDto);

    return foodInformation;
  }

}
