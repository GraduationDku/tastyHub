package com.example.tastyhub.common.domain.recipe.service;

import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.user.entity.User;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;

public interface RecipeService {

    void createRecipe(RecipeCreateDto recipeCreateDto, MultipartFile img, User user) throws Exception;

    RecipeDto getRecipe(Long recipeId);

    void updateRecipe(Long recipeId, MultipartFile img,User user, RecipeUpdateDto recipeUpdateDto) throws IOException;
    
    Page<PagingRecipeResponse> getAllRecipes(Pageable pageable);

    Page<PagingRecipeResponse> getPopularRecipes(Pageable pageable);

    Page<PagingRecipeResponse> getSearchedRecipes(String foodName, Pageable pageable);


    void deleteRecipe(Long recipeId, User user) throws IOException;
}
