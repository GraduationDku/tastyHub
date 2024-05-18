package com.example.tastyhub.common.domain.recipe.service;

import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.user.entity.User;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;

public interface RecipeService {

    void createRecipe(RecipeCreateDto recipeCreateDto, User user);

    RecipeDto getRecipe(Long recipeId);

    void updateRecipe(Long recipeId, User user, RecipeUpdateDto recipeUpdateDto);
    
    Page<PagingRecipeResponse> getAllRecipes(Pageable pageable);

    Page<PagingRecipeResponse> getPopularRecipes(Pageable pageable);

    Page<PagingRecipeResponse> getSearchedRecipes(String foodName, Pageable pageable);


    void deleteRecipe(Long recipeId, User user);
}
