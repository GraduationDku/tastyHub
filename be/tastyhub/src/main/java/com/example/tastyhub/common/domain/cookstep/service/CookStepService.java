package com.example.tastyhub.common.domain.cookstep.service;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateRequest;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import java.util.List;

public interface CookStepService {

  List<CookStep> createCookSteps(List<CookStepCreateRequest> cookSteps);


  void relationRecipe(List<CookStep> cookSteps, Recipe recipe);

  List<CookStep> updateCookStepsByRecipeUpdateDto(RecipeUpdateDto recipeUpdateDto, Recipe recipe);

  List<CookStepDto> getCookStepDtos(List<CookStep> cookSteps);
}
