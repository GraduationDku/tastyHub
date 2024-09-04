package com.example.tastyhub.common.domain.recipeReview.controller;

import static com.example.tastyhub.common.config.APIConfig.RECIPEREVIEW_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.DELETE_SUCCESS;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_CREATED;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.recipeReview.dtos.PagingMyRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewCreateRequest;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewUpdateRequest;
import com.example.tastyhub.common.domain.recipeReview.service.RecipeReviewService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;



@RestController
@RequestMapping(RECIPEREVIEW_API)
@RequiredArgsConstructor
public class RecipeReviewController {
    private final RecipeReviewService recipeReviewService;
    private final SetHttpHeaders setHttpHeaders;

    @PostMapping("/create/{recipeId}")
    public ResponseEntity<StatusResponse> createRecipeReview(
        @PathVariable Long recipeId, 
        @RequestBody RecipeReviewCreateRequest recipeReviewCreateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        recipeReviewService.createRecipeReview(recipeId, recipeReviewCreateRequest, userDetails.getUser());
        return RESPONSE_CREATED;
    }

    @GetMapping("/list/{recipeId}")
    public ResponseEntity<List<PagingRecipeReviewResponse>> getRecipeReviews(@PathVariable Long recipeId, 
    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PagingRecipeReviewResponse> pagingRecipeReviewResponseList = recipeReviewService.getRecipeReviews(recipeId);
        return ResponseEntity.ok().body(pagingRecipeReviewResponseList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PagingMyRecipeReviewResponse>> getRecipeReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PagingMyRecipeReviewResponse> pagingMyRecipeReviewResponseList = recipeReviewService.getMyRecipeReviews(userDetails.getUser());
        return ResponseEntity.ok().body(pagingMyRecipeReviewResponseList);
    }

    @PatchMapping("/modify/{recipeReviewId}")
    public ResponseEntity<StatusResponse> updateRecipeReview(@PathVariable Long recipeReviewId,
        @RequestBody RecipeReviewUpdateRequest recipeReviewUpdateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails){
            recipeReviewService.updateRecipeReview(recipeReviewId,recipeReviewUpdateRequest, userDetails.getUser());
            return RESPONSE_OK;
        
    }

    @DeleteMapping("/delete/{recipeReviewId}")
    public ResponseEntity<StatusResponse> deleteRecipeReview(@PathVariable Long recipeReviewId,
         @AuthenticationPrincipal UserDetailsImpl userDetails){
            recipeReviewService.deleteRecipeReview(recipeReviewId, userDetails.getUser());
            return DELETE_SUCCESS;
        
    }
    
}
