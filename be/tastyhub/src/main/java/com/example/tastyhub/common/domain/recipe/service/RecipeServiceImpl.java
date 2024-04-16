package com.example.tastyhub.common.domain.recipe.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
// 세현
    @Override
    public Page<PagingRecipeResponse> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAllandPaging(pageable);
    }
    @Override
    public Page<PagingRecipeResponse> getPopularRecipes(Pageable pageable) {
        return recipeRepository.findPopular(pageable);
    }

    @Override
    public Page<PagingRecipeResponse> getSearchedRecipes(String foodName, Pageable pageable) {
        return recipeRepository.searchByKeyword(foodName,pageable);
    }
// 세현

}
