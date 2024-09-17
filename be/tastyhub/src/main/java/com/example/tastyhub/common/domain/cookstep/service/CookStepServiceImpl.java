package com.example.tastyhub.common.domain.cookstep.service;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateRequest;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import com.example.tastyhub.common.domain.cookstep.repository.CookStepRepository;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookStepServiceImpl implements CookStepService {

  private final CookStepRepository cookStepRepository;

  @Override
  public List<CookStep> createCookSteps(List<CookStepCreateRequest> cookSteps) {
    return cookSteps.stream().map(CookStep::makeCookStep)
        .collect(Collectors.toList());
  }

  @Override
  public void relationRecipe(List<CookStep> cookSteps, Recipe recipe) {
    cookSteps.forEach(cookStep -> cookStep.updateRelation(recipe));
  }

  @Override
  public List<CookStep> updateCookStepsByRecipeUpdateDto(RecipeUpdateDto recipeUpdateDto, Recipe recipe) {
    List<CookStep> existingCookSteps = recipe.getCookSteps();
    Map<Long, CookStep> existingCookStepMap = existingCookSteps.stream()
        .collect(Collectors.toMap(CookStep::getId, cookStep -> cookStep));

    List<CookStepDto> cookStepUpdateDtos = recipeUpdateDto.getCookSteps();
    return cookStepUpdateDtos.stream()
        .map(dto -> {
          CookStep cookStep = existingCookStepMap.get(dto.getCookStepId());
          if (cookStep == null) {
            cookStep = new CookStep(); // 새 객체 생성
            cookStep.setRecipe(recipe);
          }
          cookStep.updateByUpdateDto(dto); // dto 정보로 기존 객체 업데이트
          return cookStep;
        })
        .collect(Collectors.toList());
  }

  @Override
  public List<CookStepDto> getCookStepDtos(List<CookStep> cookSteps) {
    return cookSteps.stream().map(CookStepDto::new)
        .collect(Collectors.toList());
  }
}
