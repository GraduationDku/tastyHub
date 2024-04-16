package com.example.tastyhub.common.domain.recipe.controller;


import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private final SetHttpHeaders setHttpHeaders;

    /**
     * writer : skyriv213 method : 레시피 생성하기
     */
    @PostMapping("/create")
    public ResponseEntity<StatusResponse> createRecipe(@RequestBody RecipeCreateDto recipeCreateDto,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        recipeService.createRecipe(recipeCreateDto, userDetails.getUser());
        return RESPONSE_OK;
    }

    /**
     * writer : skyriv213 method : 단일레시피 조회하기
     */
    @GetMapping("/detail/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable Long recipeId) {
        RecipeDto recipeDto = recipeService.getRecipe(recipeId);
        return ResponseEntity.ok().headers(setHttpHeaders.setHttpHeaderTypeJson()).body(recipeDto);
    }

    /**
     * writer : skyriv213 method : 레시피 수정하기
     */
    @PatchMapping("/modify/{recipeId}")
    public ResponseEntity<StatusResponse> updateRecipe(@PathVariable Long recipeId,@RequestBody RecipeUpdateDto recipeUpdateDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        recipeService.updateRecipe(recipeId, userDetails.getUser(), recipeUpdateDto);
        return RESPONSE_OK;
    }
}
