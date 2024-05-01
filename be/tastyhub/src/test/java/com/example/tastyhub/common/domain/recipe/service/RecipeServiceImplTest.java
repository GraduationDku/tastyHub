package com.example.tastyhub.common.domain.recipe.service;

import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE_CREATE_DTO;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
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

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Test
    @DisplayName("레시피 생성 성공")
    void createRecipe() {
        recipeService.createRecipe(RECIPE_CREATE_DTO,USER);
        verify(recipeRepository, times(1)).save(any());

    }

//    @Test
//    @DisplayName("레시피 생성 실패")
//    void createRecipeFail() {
//    }

    @Test
    @DisplayName("레시피 조회하기")
    void getRecipe() {
        given(recipeRepository.findById(RECIPE.getId())).willReturn(Optional.of(RECIPE));
        recipeService.getRecipe(RECIPE.getId());
        verify(recipeRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("레시피 조회하기 실패")
    void getRecipeFail() {
        given(recipeRepository.findById(any())).willThrow(new IllegalArgumentException("해당 레시피는 존재하지 않습니다"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recipeService.getRecipe(anyLong());
        });
        assertEquals("해당 레시피는 존재하지 않습니다",exception.getMessage());
    }
}