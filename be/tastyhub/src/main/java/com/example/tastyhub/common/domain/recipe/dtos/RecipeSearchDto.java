package com.example.tastyhub.common.domain.recipe.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeSearchDto {
    private String foodName;
}
