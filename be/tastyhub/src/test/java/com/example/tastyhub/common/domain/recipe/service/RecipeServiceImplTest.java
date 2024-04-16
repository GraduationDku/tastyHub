package com.example.tastyhub.common.domain.recipe.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Test
    @DisplayName("레시피 생성 성공")
    void createRecipe() {
    }

    @Test
    @DisplayName("레시피 생성 실패")
    void createRecipeFail() {
    }

    @Test
    @DisplayName("레시피 조회하기")
    void getRecipe() {
    }

    @Test
    @DisplayName("레시피 조회하기 실패")
    void getRecipeFail() {
    }
}