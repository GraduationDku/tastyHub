package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipe.entity.RecipeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

  private Long foodId;
  private RecipeType recipeType;
  private String foodName;
  private String foodImgUrl;
  private String foodVideoUrl;

  private boolean isLiked;
  private boolean isScraped;

  private FoodInformationDto foodInformation;

  private List<IngredientDto> ingredients;

  private List<CookStepDto> cookSteps;


  public static RecipeDto getBuild(Recipe recipe, boolean isLiked, boolean isScraped,
      FoodInformationDto foodInformationDto, List<IngredientDto> ingredients,
      List<CookStepDto> cookSteps) {
    return RecipeDto.builder()
        .foodId(recipe.getId())
        .recipeType(recipe.getRecipeType())
        .foodName(recipe.getFoodName())
        .isLiked(isLiked)
        .isScraped(isScraped)
        .foodImgUrl(recipe.getRecipeImgUrl())
        .foodInformation(foodInformationDto)
        .foodVideoUrl(recipe.getRecipeVideoUrl())
        .ingredients(ingredients)
        .cookSteps(cookSteps)
        .build();
  }
}
