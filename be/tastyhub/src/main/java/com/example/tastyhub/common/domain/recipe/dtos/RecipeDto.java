package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepResponseDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
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
    private String foodName;
    private String foodImgUrl;

    private boolean isLiked;
    private boolean isScraped;

    private FoodInformationDto foodInformation;

    private List<IngredientDto> ingredients;

    private List<CookStepResponseDto> cookSteps;


    public static RecipeDto getBuild(Recipe recipe, boolean isLiked, boolean isScraped,
        FoodInformationDto foodInformationDto, List<IngredientDto> ingredients,
        List<CookStepResponseDto> cookSteps) {
        return RecipeDto.builder()
            .foodId(recipe.getId())
            .foodName(recipe.getFoodName())
            .isLiked(isLiked)
            .isScraped(isScraped)
            .foodImgUrl(recipe.getFoodImgUrl())
            .foodInformation(foodInformationDto)
            .ingredients(ingredients)
            .cookSteps(cookSteps)
            .build();

    }
}
