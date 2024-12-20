package com.example.tastyhub.common.domain.recipe.service;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import com.example.tastyhub.common.domain.cookstep.service.CookStepService;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import com.example.tastyhub.common.domain.foodInformation.service.FoodInformationService;
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
import com.example.tastyhub.common.utils.S3.S3Uploader;

import com.example.tastyhub.common.utils.page.RestPage;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

  private final RecipeRepository recipeRepository;

  private final FoodInformationService foodInformationService;

  private final CookStepService cookStepService;

  private final IngredientService ingredientService;

  private final S3Uploader s3Uploader;

  @Override
  @Transactional
  public void createRecipe(RecipeCreateDto recipeCreateDto, MultipartFile recipeImg, List<MultipartFile> cookStepImgs, User user)
      throws Exception {
    String imgUrl = new String();

    FoodInformation foodInformation = foodInformationService.createFoodInformation(recipeCreateDto);

    List<Ingredient> ingredients = ingredientService.createIngredients(
        recipeCreateDto.getIngredients());
    log.info(String.valueOf(cookStepImgs.size()));
    List<CookStep> cookSteps = cookStepService.createCookSteps(recipeCreateDto.getRecipeType(),recipeCreateDto.getCookSteps(),cookStepImgs);

    try {
      imgUrl = s3Uploader.upload(recipeImg, "image/recipeImg");

      Recipe recipe = Recipe.createRecipe(recipeCreateDto, user, imgUrl, foodInformation,
          ingredients,
          cookSteps);

      foodInformationService.relationRecipe(foodInformation, recipe);
      ingredientService.relationRecipe(ingredients, recipe);
      cookStepService.relationRecipe(cookSteps, recipe);

      recipeRepository.save(recipe);
    } catch (Exception e) {
      // 레시피 저장에 실패한 경우, S3에서 이미지 삭제
      deleteImgInS3(imgUrl);
      throw e; // 예외를 다시 던져 트랜잭션 롤백 활성화
    }
  }


  @Override
  @Transactional
  public RecipeDto getRecipe(Long recipeId, User user) {
    Recipe recipe = recipeFindById(recipeId);
    boolean isLiked = recipeRepository.isLiked(user.getId(), recipeId);
    boolean isScraped = recipeRepository.isScraped(user.getId(), recipeId);

    FoodInformationDto foodInformationDto = foodInformationService.getFoodInformationDto(recipe);
    List<IngredientDto> ingredients = ingredientService.getIngredientDtos(recipe.getIngredients());
    List<CookStepDto> cookSteps = cookStepService.getCookStepDtos(recipe.getCookSteps());

    return RecipeDto.getBuild(recipe, isLiked, isScraped, foodInformationDto, ingredients,
        cookSteps);
  }

  @Override
  @Transactional
  @CacheEvict(value = "popularRecipes", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public void updateRecipe(Long recipeId, MultipartFile img, User user,
      RecipeUpdateDto recipeUpdateDto) throws java.io.IOException {
    Recipe recipe = checkRecipeAndUser(recipeId, user);
    String originalImgUrl = recipe.getRecipeImgUrl();
    String imgUrl = new String();
    try {
      imgUrl = s3Uploader.upload(img, "image/recipeImg");
      FoodInformation updateFoodInformation = foodInformationService.updateFoodInformation(
          recipeUpdateDto.getFoodInformation(), recipe.getFoodInformation());

      // 기존 Ingredient 리스트 처리
      List<Ingredient> updatedIngredients = ingredientService.updatedIngredients(
          recipeUpdateDto, recipe);

      // 기존 CookStep 리스트 처리
      List<CookStep> updatedCookSteps = cookStepService.updateCookStepsByRecipeUpdateDto(
          recipeUpdateDto, recipe);

      // Recipe 객체에 대한 최종 업데이트 호출
      recipe.update(updateFoodInformation, updatedIngredients, updatedCookSteps,
          recipeUpdateDto.getFoodName(), imgUrl);

      s3Uploader.delete(originalImgUrl);
    } catch (Exception e) {
      // 레시피 저장에 실패한 경우, S3에서 이미지 삭제
      deleteImgInS3(imgUrl);
    }

  }

  private void deleteImgInS3(String imgUrl) throws java.io.IOException {
    if (!imgUrl.isEmpty()) {
      try {
        s3Uploader.delete(imgUrl);
      } catch (IOException ioException) {
        log.error("Failed to delete uploaded image from S3", ioException);
      }
    }
  }

  // 세현

  @Override
  @Transactional
  public Page<PagingRecipeResponse> getAllRecipes(Pageable pageable) {
    return recipeRepository.findAllandPaging(pageable);
  }

  @Override
  public Page<PagingRecipeResponse> getMyRecipes(Pageable pageable, User user) {
    return recipeRepository.findMyRecipes(pageable, user.getId());

  }

  @Override
  @Transactional
  @Cacheable(value = "popularRecipes", key = "#pageable.pageNumber + '-' + #pageable.pageSize", unless = "#result == null || #result.isEmpty()")
  public RestPage<PagingRecipeResponse> getPopularRecipes(Pageable pageable) {
    return recipeRepository.findPopular(pageable);
  }

  @Override
  @Transactional
  public Page<PagingRecipeResponse> getSearchedRecipes(String foodName, Pageable pageable) {
    return recipeRepository.searchByKeyword(foodName, pageable);
  }

  @Override
  @Transactional
  @CacheEvict(value = "popularRecipes", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public void deleteRecipe(Long recipeId, User user) throws java.io.IOException {
    Recipe recipe = checkRecipeAndUser(recipeId, user);
    String imgUrl = recipe.getRecipeImgUrl();
    recipeRepository.delete(recipe);
    s3Uploader.delete(imgUrl);
  }

  @Override
  public Recipe findById(Long recipeId) {
    return recipeRepository.findById(recipeId)
        .orElseThrow(() -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다."));
  }

  public Recipe checkRecipeAndUser(Long recipeId, User user) {
    Recipe recipe = recipeFindById(recipeId);
    if (!checkRecipeOwner(user, recipe)) {
      throw new IllegalArgumentException("해당 유저는 접근권한이 없습니다.");
    }
    return recipe;
  }

  private static boolean checkRecipeOwner(User user, Recipe recipe) {
    return recipe.getUser().getUsername().equals(user.getUsername());
  }

  // 세현
  @Generated
  private Recipe recipeFindById(Long recipeId) {
    return recipeRepository.findById(recipeId)
        .orElseThrow(() -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다"));
  }


}