package com.example.tastyhub.common.domain.recipe.controller;

import static com.example.tastyhub.common.config.APIConfig.RECIPE_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeSearchDto;
import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import com.example.tastyhub.common.utils.SetHttpHeaders;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(RECIPE_API)
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final SetHttpHeaders httpHeaders;

    @GetMapping("/popural")
    public ResponseEntity<Page<PagingRecipeResponse>> getPopuralRecipes(
            @PageableDefault(size = 7, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().headers(httpHeaders.setHttpHeaderTypeJson())
                .body(recipeService.getPopularRecipes(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PagingRecipeResponse>> getAllRecipes(
            @PageableDefault(size = 100, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().headers(httpHeaders.setHttpHeaderTypeJson())
                .body(recipeService.getAllRecipes(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PagingRecipeResponse>> getSearchedRecipes(@RequestBody RecipeSearchDto recipeSearchDto,
            @PageableDefault(size = 100, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().headers(httpHeaders.setHttpHeaderTypeJson()).body(
                recipeService.getSearchedRecipes(recipeSearchDto.getFoodName(), pageable));
    }

}
