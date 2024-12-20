package com.example.tastyhub.common.domain.recipe.service;

import static com.example.tastyhub.fixture.recipe.RecipeFixture.PAGING_RECIPE_RESPONSE_PAGE;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE_CREATE_DTO;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tastyhub.common.domain.cookstep.service.CookStepService;
import com.example.tastyhub.common.domain.foodInformation.service.FoodInformationService;
import com.example.tastyhub.common.domain.ingredient.service.IngredientService;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.utils.S3.S3Uploader;

import java.util.ArrayList;
import java.util.Optional;
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

    @Mock
    FoodInformationService foodInformationService;
    @Mock
    CookStepService cookStepService;
    @Mock
    IngredientService ingredientService;

    @Mock
    S3Uploader s3Uploader;

    @InjectMocks
    RecipeServiceImpl recipeService;

   @Test
   @DisplayName("레시피 생성 성공")
   void createRecipe() throws Exception {
     when(s3Uploader.upload(any(), any())).thenReturn("someUrl");
     recipeService.createRecipe(RECIPE_CREATE_DTO, null, new ArrayList<>(), USER);
     verify(recipeRepository, times(1)).save(any());

   }

    @Test
    @DisplayName("레시피 조회하기")
    void getRecipe() {
        given(recipeRepository.findById(RECIPE.getId())).willReturn(Optional.of(RECIPE));
        recipeService.getRecipe(RECIPE.getId(),USER);
        verify(recipeRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("레시피 조회하기 실패")
    void getRecipeFail() {
        given(recipeRepository.findById(any())).willThrow(new IllegalArgumentException("해당 레시피는 존재하지 않습니다"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recipeService.getRecipe(anyLong(),USER);
        });
        assertEquals("해당 레시피는 존재하지 않습니다",exception.getMessage());
    }


    @Test
    @DisplayName("모든 레시피 조회 성공")
    void getAllRecipes() {
        given(recipeRepository.findAllandPaging(any())).willReturn(PAGING_RECIPE_RESPONSE_PAGE);
        recipeService.getAllRecipes(any());
        verify(recipeRepository, times(1)).findAllandPaging(any());
    }

    @Test
    @DisplayName("인기 레시피 조회 성공")
    void getPopularRecipes() {
        given(recipeRepository.findPopular(any())).willReturn(PAGING_RECIPE_RESPONSE_PAGE);
        recipeService.getPopularRecipes(any());
        verify(recipeRepository, times(1)).findPopular(any());
    }

    @Test
    @DisplayName("검색 레시피 조회 성공")
    void getSearchedRecipes() {
        given(recipeRepository.searchByKeyword(any(), any())).willReturn(PAGING_RECIPE_RESPONSE_PAGE);
        recipeService.getSearchedRecipes(any(), any());
        verify(recipeRepository, times(1)).searchByKeyword(any(),any());
    }

}