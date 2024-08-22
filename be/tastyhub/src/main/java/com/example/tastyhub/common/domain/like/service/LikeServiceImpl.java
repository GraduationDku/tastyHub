package com.example.tastyhub.common.domain.like.service;

import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.like.dtos.LikeCountRequest;
import com.example.tastyhub.common.domain.like.entity.Like;
import com.example.tastyhub.common.domain.like.repository.LikeRepository;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final RecipeService recipeService;



    @Override
    @Transactional
    public boolean like(Long recipeId, User user) {
        Recipe recipe = recipeService.findById(recipeId);
        Long userId = user.getId();
        if(checkLike(recipeId, userId)){
            likeRepository.deleteByRecipeIdAndUserId(recipeId, userId);
            return true;
        }
        Like like = Like.builder().user(user).recipe(recipe).build();
        likeRepository.save(like);
        return false;
    }


    private boolean checkLike(Long recipeId, Long userId) {
        return likeRepository.existsByRecipeIdAndUserId(recipeId, userId);
    }


    @Override
    public LikeCountRequest count(Long recipeId) {
        LikeCountRequest likeCountRequest =  LikeCountRequest.builder().count(likeRepository.countByRecipeId(recipeId)).build();
        return likeCountRequest;
    }
}
