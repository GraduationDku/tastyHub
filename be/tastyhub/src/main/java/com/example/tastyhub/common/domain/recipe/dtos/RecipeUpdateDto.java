package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepUpdateDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class RecipeUpdateDto {

    private String foodName;

    private MultipartFile foodImg;

    private FoodInformationDto foodInformation;

    private List<IngredientDto> ingredients;

    private List<CookStepUpdateDto> cookSteps;

}
