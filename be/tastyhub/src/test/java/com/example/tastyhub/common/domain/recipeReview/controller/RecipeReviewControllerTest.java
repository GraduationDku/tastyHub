package com.example.tastyhub.common.domain.recipeReview.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.RECIPEREVIEW_API;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.PAGING_RECIPE_REVIEW_RESPONSES;
import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.RECIPE_REVIEW;
import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.RECIPE_REVIEW_CREATE_REQUEST;
import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.RECIPE_REVIEW_UPDATE_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.comment.controller.CommentController;
import com.example.tastyhub.common.domain.comment.service.CommentService;
import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import com.example.tastyhub.common.domain.recipeReview.service.RecipeReviewService;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = RecipeReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class RecipeReviewControllerTest {


  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  SetHttpHeaders setHttpHeaders;

  @MockBean
  RecipeReviewService recipeReviewService;


  @Test
  @WithCustomMockUser
  void createRecipeReview() throws Exception {
    doNothing().when(recipeReviewService).createRecipeReview(eq(RECIPE.getId()), eq(RECIPE_REVIEW_CREATE_REQUEST), any());

    ResultActions resultActions = mockMvc.perform(post(RECIPEREVIEW_API+"/create/{recipeId}", RECIPE.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(RECIPE_REVIEW_CREATE_REQUEST))
            .with(csrf()))
        .andExpect(status().isCreated());

    resultActions.andDo(document("recipeReviewController/create",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("recipeId").description("The ID of the recipe")
        ),
        requestFields(
            fieldWithPath("grade").type(JsonFieldType.NUMBER).description("Review grade"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("Review content")
        )
    ));
  }

  @Test
  @WithCustomMockUser
  void getRecipeReviews() throws Exception {
    when(recipeReviewService.getRecipeReviews(eq(RECIPE.getId()), any())).thenReturn(PAGING_RECIPE_REVIEW_RESPONSES);

    ResultActions resultActions = mockMvc.perform(get(RECIPEREVIEW_API + "/list/{recipeId}", RECIPE.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    resultActions.andDo(document("recipe-review/get-list",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("recipeId").description("The ID of the recipe")
        ),
        responseFields(
            fieldWithPath("content[].nickname").type(JsonFieldType.STRING).description("Nickname of the reviewer"),
            fieldWithPath("content[].grade").type(JsonFieldType.NUMBER).description("Review grade"),
            fieldWithPath("content[].content").type(JsonFieldType.STRING).description("Review content"),
            fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("Current page number"),
            fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("Page size"),
            fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("Is sort empty"),
            fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("Is unsorted"),
            fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("Is sorted"),
            fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("Offset"),
            fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("Is paged"),
            fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("Is unpaged"),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("Is this the last page"),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("Total number of pages"),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("Total number of elements"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("Is this the first page"),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("Size of the page"),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("Page number"),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("Is sort empty"),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("Is unsorted"),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("Is sorted"),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("Number of elements on this page"),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("Is the page empty")
        )
    ));
  }


  @Test
  @WithCustomMockUser
  void updateRecipeReview() throws Exception {
    doNothing().when(recipeReviewService).updateRecipeReview(eq(RECIPE_REVIEW.getId()), eq(RECIPE_REVIEW_UPDATE_REQUEST), any());

    ResultActions resultActions = mockMvc.perform(patch(RECIPEREVIEW_API+"/modify/{recipeReviewId}", RECIPE_REVIEW.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(RECIPE_REVIEW_UPDATE_REQUEST))
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("recipe-review/modify",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("recipeReviewId").description("The ID of the recipe review")
        ),
        requestFields(
            fieldWithPath("grade").type(JsonFieldType.NUMBER).description("Updated review grade"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("Updated review content")
        )
    ));
  }

  @Test
  @WithCustomMockUser
  void deleteRecipeReview() throws Exception {
    doNothing().when(recipeReviewService).deleteRecipeReview(eq(RECIPE_REVIEW.getId()), any());

    ResultActions resultActions = mockMvc.perform(delete(RECIPEREVIEW_API+"/delete/{recipeReviewId}", RECIPE_REVIEW.getId())
            .with(csrf()))
        .andExpect(status().isNoContent());

    resultActions.andDo(document("recipe-review/delete",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("recipeReviewId").description("The ID of the recipe review")
        )
    ));
  }
}