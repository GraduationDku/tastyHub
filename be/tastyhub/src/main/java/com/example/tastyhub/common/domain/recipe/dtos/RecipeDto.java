package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepResponseDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeDto {

    private Long foodId;
    private String foodName;
    private String foodImgUrl;

    private FoodInformationDto foodInformation;

    private List<IngredientDto> ingredients;

    private List<CookStepResponseDto> cookSteps;

}
