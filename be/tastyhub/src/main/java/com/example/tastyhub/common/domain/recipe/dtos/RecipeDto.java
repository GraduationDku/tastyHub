package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepResponseDto;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepUpdateDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationResponseDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientResponseDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientUpdateDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeDto {

    private Long foodId;
    private String foodName;
    private String foodImgUrl;

    private FoodInformationResponseDto foodInformation;

    private List<IngredientResponseDto> ingredients;

    private List<CookStepResponseDto> cookSteps;

}
