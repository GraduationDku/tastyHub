package com.example.tastyhub.common.domain.recipe.service;

import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.user.entity.User;

public interface RecipeService {

    void createRecipe(RecipeCreateDto recipeCreateDto, User user);

    RecipeDto getRecipe(Long recipeId);

    void updateRecipe(Long recipeId, User user, RecipeUpdateDto recipeUpdateDto);
}
