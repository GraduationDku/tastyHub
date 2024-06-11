package com.example.tastyhub.common.domain.like.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tastyhub.common.domain.like.repository.LikeRepository;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;

@ExtendWith(MockitoExtension.class)
public class LikeServiceImplTest {
    @Mock
    LikeRepository likeRepository;

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    LikeServiceImpl likeService;

    @Test
    @DisplayName("좋아요가 존재할 때 삭제")
    void likeAlreadyExists() {
        when(recipeRepository.findById(RECIPE.getId())).thenReturn(Optional.of(RECIPE));
        when(likeRepository.existsByRecipeIdAndUserId(RECIPE.getId(),USER.getId())).thenReturn(true);
        likeService.like(RECIPE.getId(),USER);

        // then
        verify(likeRepository, times(1)).deleteByRecipeIdAndUserId(any(), any());
        verify(likeRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("좋아요가 존재하지 않을 때 생성")
    void likeDoesNotExist() {
        when(recipeRepository.findById(RECIPE.getId())).thenReturn(Optional.of(RECIPE));
        when(likeRepository.existsByRecipeIdAndUserId(RECIPE.getId(),USER.getId())).thenReturn(false);

        // when
        likeService.like(RECIPE.getId(),USER);

        // then
        verify(likeRepository, times(0)).deleteByRecipeIdAndUserId(any(), any());
        verify(likeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("레시피의 좋아요 개수 조회")
    void countLike(){
        when(likeRepository.countByRecipeId(RECIPE.getId())).thenReturn(any());

        likeService.count(RECIPE.getId());
        verify(likeRepository, times(1)).countByRecipeId(RECIPE.getId());
    }
}
