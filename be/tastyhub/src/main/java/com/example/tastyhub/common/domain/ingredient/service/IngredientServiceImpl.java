package com.example.tastyhub.common.domain.ingredient.service;

import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

  @Override
  public List<Ingredient> createIngredients(List<IngredientCreateDto> ingredients) {
    return ingredients.stream().map(Ingredient::makeIngredient)
        .collect(Collectors.toList());
  }

  @Override
  public void relationRecipe(List<Ingredient> ingredients, Recipe recipe) {
    ingredients.forEach(ingredient -> ingredient.updateRelation(recipe));
  }

  @Override
  public List<Ingredient> updatedIngredients(RecipeUpdateDto recipeUpdateDto, Recipe recipe) {

    List<Ingredient> existingIngredients = recipe.getIngredients();
    Map<Long, Ingredient> existingIngredientMap = existingIngredients.stream()
        .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));

    List<IngredientDto> ingredientDtos = recipeUpdateDto.getIngredients();
    return ingredientDtos.stream()
        .map(dto -> {
          Ingredient ingredient = existingIngredientMap.get(dto.getIngredientId());
          if (ingredient == null) {
            ingredient = new Ingredient(); // 새 객체 생성
          }
          ingredient.updateByUpdateDto(dto); // dto 정보로 기존 객체 업데이트
          return ingredient;
        })
        .collect(Collectors.toList());

  }

  @Override
  public List<IngredientDto> getIngredientDtos(List<Ingredient> ingredients) {
    return ingredients.stream()
        .map(IngredientDto::new)
        .collect(Collectors.toList());
  }
}
