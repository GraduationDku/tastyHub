package com.example.tastyhub.common.domain.recipe.service;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateDto;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepResponseDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;
import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;


    @Override
    @Transactional
    public void createRecipe(RecipeCreateDto recipeCreateDto, User user) {
        FoodInformation foodInformation = FoodInformation.builder()
            .text(recipeCreateDto.getFoodInformation().getText())
            .serving(recipeCreateDto.getFoodInformation().getServing())
            .cookingTime(recipeCreateDto.getFoodInformation().getCookingTime())
            .build();

        List<IngredientCreateDto> ingredientCreateDtos = recipeCreateDto.getIngredients();
        List<CookStepCreateDto> cookStepCreateDtos = recipeCreateDto.getCookSteps();

        List<Ingredient> ingredients = ingredientCreateDtos.stream().map(Ingredient::makeIngredient)
            .collect(Collectors.toList());

        List<CookStep> cookSteps = cookStepCreateDtos.stream().map(CookStep::makeCookStep)
            .collect(Collectors.toList());

        Recipe recipe = Recipe.builder()
            .foodName(recipeCreateDto.getFoodName())
            .foodImgUrl("refactoring")
            .user(user)

            .foodInformation(foodInformation)
            .ingredients(ingredients)
            .cookSteps(cookSteps)
            .build();

        recipeRepository.save(recipe);
    }

    @Override
    @Transactional
    public RecipeDto getRecipe(Long recipeId) {
        Recipe recipe = recipeFindById(recipeId);
        FoodInformationDto foodInformationDto = FoodInformationDto.builder()
            .foodInformationId(recipe.getFoodInformation().getId())
            .text(recipe.getFoodInformation().getText())
            .cookingTime(recipe.getFoodInformation().getCookingTime())
            .serving(recipe.getFoodInformation().getServing())
            .build();

        List<Ingredient> ingredientList = recipe.getIngredients();
        List<CookStep> cookStepList = recipe.getCookSteps();

        List<IngredientDto> ingredients = ingredientList.stream()
            .map(IngredientDto::new)
            .collect(Collectors.toList());

        List<CookStepResponseDto> cookSteps = cookStepList.stream().map(CookStepResponseDto::new)
            .collect(Collectors.toList());

        RecipeDto recipeDto = RecipeDto.builder()
            .foodId(recipe.getId())
            .foodName(recipe.getFoodName())
            .foodImgUrl(recipe.getFoodImgUrl())
            .foodInformation(foodInformationDto)
            .ingredients(ingredients)
            .cookSteps(cookSteps)
            .build();
        return recipeDto;
    }

    private Recipe recipeFindById(Long recipeId) {
        return recipeRepository.findById(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다"));
    }

    @Override
    public void updateRecipe(Long recipeId, User user, RecipeUpdateDto recipeUpdateDto) {
//        Recipe recipe = recipeFindById(recipeId);
//        if (!recipe.getUser().equals(user)) {
//            throw new IllegalArgumentException("해당 유저는 접근권한이 없습니다.");
//        }
//
//        FoodInformationUpdateDto foodInformation = recipeUpdateDto.getFoodInformation();
//
//
//        List<IngredientUpdateDto> ingredientUpdateDtoList = recipeUpdateDto.getIngredients();
//        List<CookStepUpdateDto> cookStepUpdateDtoList = recipeUpdateDto.getCookSteps();
//
//        List<Object> ingredients = ingredientUpdateDtoList.stream().map(Ingredient::new)
//            .collect(Collectors.toList());
//
//        List<CookStep> cookSteps = cookStepUpdateDtoList.stream().map(CookStep::new)
//            .collect(Collectors.toList());
//
//        recipe.update();


    }
    // 세현
    @Override
    @Transactional
    public Page<PagingRecipeResponse> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAllandPaging(pageable);
    }
    @Override
    @Transactional
    public Page<PagingRecipeResponse> getPopularRecipes(Pageable pageable) {
        return recipeRepository.findPopular(pageable);
    }

    @Override
    @Transactional
    public Page<PagingRecipeResponse> getSearchedRecipes(String foodName, Pageable pageable) {
        return recipeRepository.searchByKeyword(foodName,pageable);
    }
// 세현


}
