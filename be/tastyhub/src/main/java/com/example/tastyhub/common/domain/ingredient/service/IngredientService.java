package com.example.tastyhub.common.domain.ingredient.service;

import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import java.util.List;

public interface IngredientService {

  List<Ingredient> createIngredients(List<IngredientCreateDto> ingredients);

  void relationRecipe(List<Ingredient> ingredients, Recipe recipe);

  List<Ingredient> updatedIngredients(RecipeUpdateDto recipeUpdateDto, Recipe recipe);

  List<IngredientDto> getIngredientDtos(List<Ingredient> ingredients);
}
