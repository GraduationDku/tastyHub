package com.example.tastyhub.common.domain.scrap.service;

import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.scrap.repository.ScrapRepository;

@ExtendWith(MockitoExtension.class)
public class ScrapServiceImplTest {
    @Mock
    ScrapRepository scrapRepository;

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    ScrapServiceImpl scrapService;

    @Test
    @DisplayName("스크랩이 존재할 때 삭제")
    void scrapAlreadyExists() {
        when(recipeRepository.findById(RECIPE.getId())).thenReturn(Optional.of(RECIPE));
        when(scrapRepository.existsByRecipeIdAndUserId(RECIPE.getId(),USER.getId())).thenReturn(true);

        // when
        scrapService.scrap(RECIPE.getId(),USER);

        // then
        verify(scrapRepository, times(1)).deleteByRecipeIdAndUserId(any(), any());
        verify(scrapRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("스크랩이 존재하지 않을 때 생성")
    void scrapDoesNotExist() {
        when(recipeRepository.findById(RECIPE.getId())).thenReturn(Optional.of(RECIPE));

        when(scrapRepository.existsByRecipeIdAndUserId(USER.getId(), RECIPE.getId())).thenReturn(false);

        // when
        scrapService.scrap(RECIPE.getId(),USER);

        // then
        verify(scrapRepository, times(0)).deleteByRecipeIdAndUserId(any(), any());
        verify(scrapRepository, times(1)).save(any());
    }
}
