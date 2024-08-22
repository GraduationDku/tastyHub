package com.example.tastyhub.common.domain.scrap.service;

import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.scrap.dtos.PagingScrapResponse;
import com.example.tastyhub.common.domain.scrap.entity.Scrap;
import com.example.tastyhub.common.domain.scrap.repository.ScrapRepository;
import com.example.tastyhub.common.domain.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService{

    private final ScrapRepository scrapRepository;
    private final RecipeService recipeService;

    @Override
    @Transactional
    public boolean scrap(Long recipeId, User user) {
        Recipe recipe = recipeService.findById(recipeId);
        Long userId= user.getId();
        if (checkScrap(recipeId, userId)){
            scrapRepository.deleteByRecipeIdAndUserId(recipeId, userId);
            return true;
        } 
        Scrap scrap = Scrap.builder().user(user).recipe(recipe).build();
        scrapRepository.save(scrap);
        return false;
    }

    public boolean checkScrap(Long recipeId, Long userId){
        return scrapRepository.existsByRecipeIdAndUserId(recipeId, userId);
    }

    @Override
    public Page<PagingScrapResponse> getScrapList(User user, Pageable pageable) {
        return scrapRepository.findScrapedRecipe(pageable, user.getId());

    }

}
