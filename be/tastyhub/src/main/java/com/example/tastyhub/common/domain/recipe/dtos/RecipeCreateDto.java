package com.example.tastyhub.common.domain.recipe.dtos;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class RecipeCreateDto {

    private String foodName;

    private MultipartFile foodImg;

    private FoodInformationCreateDto foodInformation;

    private List<IngredientCreateDto> ingredients;

    private List<CookStepCreateDto> cookSteps;


}
