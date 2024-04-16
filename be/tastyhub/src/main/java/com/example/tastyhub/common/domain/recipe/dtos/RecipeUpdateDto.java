package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateDto;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepUpdateDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationCreateDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationUpdateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientUpdateDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class RecipeUpdateDto {

    private String foodName;

    private MultipartFile foodImg;

    private FoodInformationUpdateDto foodInformation;

    private List<IngredientUpdateDto> ingredients;

    private List<CookStepUpdateDto> cookSteps;

}
