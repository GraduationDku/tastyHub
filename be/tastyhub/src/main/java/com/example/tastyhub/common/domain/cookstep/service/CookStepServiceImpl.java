package com.example.tastyhub.common.domain.cookstep.service;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateRequest;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import com.example.tastyhub.common.domain.cookstep.repository.CookStepRepository;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.utils.S3.S3Uploader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CookStepServiceImpl implements CookStepService {

  private final S3Uploader s3Uploader;

  @Override
  public List<CookStep> createCookSteps(List<CookStepCreateRequest> cookStepRequests,
      List<MultipartFile> cookStepImgs) {
    List<CookStep> cookSteps = new ArrayList<>();
    for (int i = 0; i < cookStepImgs.size(); i++) {
      try {
        String imgUrl = s3Uploader.upload(cookStepImgs.get(i), "image/cookStepImg");
        cookSteps.add(CookStep.makeCookStep(cookStepRequests.get(i), imgUrl));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return cookSteps;
  }

  @Override
  public void relationRecipe(List<CookStep> cookSteps, Recipe recipe) {
    cookSteps.forEach(cookStep -> cookStep.updateRelation(recipe));
  }

  @Override
  public List<CookStep> updateCookStepsByRecipeUpdateDto(RecipeUpdateDto recipeUpdateDto,
      Recipe recipe) {
    List<CookStep> existingCookSteps = recipe.getCookSteps();
    Map<Long, CookStep> existingCookStepMap = existingCookSteps.stream()
        .collect(Collectors.toMap(CookStep::getStepNumber, cookStep -> cookStep));

    List<CookStepDto> cookStepUpdateDtos = recipeUpdateDto.getCookSteps();
    return cookStepUpdateDtos.stream()
        .map(dto -> {
          CookStep cookStep = existingCookStepMap.get(dto.getStepNumber());
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
