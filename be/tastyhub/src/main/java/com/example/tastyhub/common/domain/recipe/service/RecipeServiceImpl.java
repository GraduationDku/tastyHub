package com.example.tastyhub.common.domain.recipe.service;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateDto;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepResponseDto;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepUpdateDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;
import com.example.tastyhub.common.domain.ingredient.service.IngredientService;
import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Generated;
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

    @Override
    @Transactional
    public void updateRecipe(Long recipeId, User user, RecipeUpdateDto recipeUpdateDto) {
        Recipe recipe = checkRecipeAndUser(recipeId, user);

        // 기존 Ingredient 리스트 처리
        List<Ingredient> updatedIngredients = getUpdatedIngredients(
            recipeUpdateDto, recipe);

        // 기존 CookStep 리스트 처리
        List<CookStep> updatedCookSteps = getCookSteps(
            recipeUpdateDto, recipe);

        // Recipe 객체에 대한 최종 업데이트 호출
        recipe.update(updatedIngredients, updatedCookSteps);
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
        return recipeRepository.searchByKeyword(foodName, pageable);
    }

    @Override
    @Transactional
    public void deleteRecipe(Long recipeId, User user) {
        Recipe recipe = checkRecipeAndUser(recipeId, user);
        recipeRepository.delete(recipe);
    }

    public Recipe checkRecipeAndUser(Long recipeId, User user) {
        Recipe recipe = recipeFindById(recipeId);
        if (!recipe.getUser().equals(user)) {
            throw new IllegalArgumentException("해당 유저는 접근권한이 없습니다.");
        }
        return recipe;
    }

    // 세현
    @Generated
    private static List<CookStep> getCookSteps(RecipeUpdateDto recipeUpdateDto, Recipe recipe) {
        List<CookStep> existingCookSteps = recipe.getCookSteps();
        Map<Long, CookStep> existingCookStepMap = existingCookSteps.stream()
            .collect(Collectors.toMap(CookStep::getId, cookStep -> cookStep));

        List<CookStepUpdateDto> cookStepUpdateDtos = recipeUpdateDto.getCookSteps();
        List<CookStep> updatedCookSteps = cookStepUpdateDtos.stream()
            .map(dto -> {
                CookStep cookStep = existingCookStepMap.get(dto.getCookStepId());
                if (cookStep == null) {
                    cookStep = new CookStep(); // 새 객체 생성
                }
                cookStep.update(dto); // dto 정보로 기존 객체 업데이트
                return cookStep;
            })
            .collect(Collectors.toList());
        return updatedCookSteps;
    }

    @Generated
    private static List<Ingredient> getUpdatedIngredients(RecipeUpdateDto recipeUpdateDto,
        Recipe recipe) {
        List<Ingredient> existingIngredients = recipe.getIngredients();
        Map<Long, Ingredient> existingIngredientMap = existingIngredients.stream()
            .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));

        List<IngredientDto> ingredientDtos = recipeUpdateDto.getIngredients();
        List<Ingredient> updatedIngredients = ingredientDtos.stream()
            .map(dto -> {
                Ingredient ingredient = existingIngredientMap.get(dto.getIngredientId());
                if (ingredient == null) {
                    ingredient = new Ingredient(); // 새 객체 생성
                }
                ingredient.update(dto); // dto 정보로 기존 객체 업데이트
                return ingredient;
            })
            .collect(Collectors.toList());
        return updatedIngredients;
    }

    @Generated
    private Recipe recipeFindById(Long recipeId) {
        return recipeRepository.findById(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다"));
    }
}
