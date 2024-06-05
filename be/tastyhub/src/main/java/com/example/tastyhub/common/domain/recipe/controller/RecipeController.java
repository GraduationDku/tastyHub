package com.example.tastyhub.common.domain.recipe.controller;

import static com.example.tastyhub.common.config.APIConfig.RECIPE_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeUpdateDto;
import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
@RequestMapping(RECIPE_API)
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;
    private final SetHttpHeaders setHttpHeaders;

    @GetMapping("/popular")
    public ResponseEntity<Page<PagingRecipeResponse>> getPopuralRecipes(
        @PageableDefault(size = 7, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().headers(setHttpHeaders.setHttpHeaderTypeJson())
            .body(recipeService.getPopularRecipes(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PagingRecipeResponse>> getAllRecipes(
        @PageableDefault(size = 100, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().headers(setHttpHeaders.setHttpHeaderTypeJson())
            .body(recipeService.getAllRecipes(pageable));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<PagingRecipeResponse>> getSearchedRecipes(
        @PathVariable String keyword,
        @PageableDefault(size = 100, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        log.info("CTL : " + keyword);
        return ResponseEntity.ok().headers(setHttpHeaders.setHttpHeaderTypeJson()).body(
            recipeService.getSearchedRecipes(keyword, pageable));
    }

    /**
     * writer : skyriv213 method : 레시피 생성하기
     */
    @PostMapping("/create")
    public ResponseEntity<StatusResponse> createRecipe(@RequestPart("img") MultipartFile img, @RequestPart("data") RecipeCreateDto recipeCreateDto,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        try {
            recipeService.createRecipe(recipeCreateDto,img, userDetails.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
     * @throws IOException 
     */
    @PatchMapping("/modify/{recipeId}")
    public ResponseEntity<StatusResponse> updateRecipe(@PathVariable Long recipeId, @RequestPart("img") MultipartFile img,
        @RequestPart("data") RecipeUpdateDto recipeUpdateDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//
        recipeService.updateRecipe(recipeId, img, userDetails.getUser(), recipeUpdateDto);
        return RESPONSE_OK;
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<StatusResponse> deleteRecipe(@PathVariable Long recipeId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        recipeService.deleteRecipe(recipeId, userDetails.getUser());
        return RESPONSE_OK;
    }
}
