package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeUpdateDto {

    private String foodName;

    private String foodVideoUrl;

    private FoodInformationDto foodInformation;

    private List<IngredientDto> ingredients;

    private List<CookStepDto> cookSteps;

}
