package com.example.tastyhub.common.domain.like.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.LIKE_API;
import static com.example.tastyhub.fixture.like.LikeFixture.LIKE_CHECK_DTO;
import static com.example.tastyhub.fixture.like.LikeFixture.LIKE_COUNT_REQUEST;
import static com.example.tastyhub.fixture.post.PostFixture.POST;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.comment.controller.CommentController;
import com.example.tastyhub.common.domain.comment.service.CommentService;
import com.example.tastyhub.common.domain.like.service.LikeService;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = LikeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class LikeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  LikeService likeService;


  @MockBean
  SetHttpHeaders setHttpHeaders;


  @Test
  @WithCustomMockUser
  @DisplayName("좋아요 성공")
  void like() throws Exception {
    // Given
    given(likeService.like(eq(RECIPE.getId()), any())).willReturn(LIKE_CHECK_DTO);

    // When
    ResultActions resultActions = mockMvc.perform(post(LIKE_API + "/{recipeId}", RECIPE.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());

    // Then
    resultActions.andDo(document("likeController/{recipeId}",
        getDocumentRequest(),
        getDocumentResponse(),
        responseFields(
            fieldWithPath("check").type(JsonFieldType.BOOLEAN).description("좋아요 확인 여부")
        )));
  }


  @Test
  @WithCustomMockUser
  @DisplayName("좋아요 누적 갯수 반환")
  void count() throws Exception {

    given(likeService.count(any())).willReturn(LIKE_COUNT_REQUEST);
    ResultActions resultActions = mockMvc.perform(get(LIKE_API + "/count/{recipeId}", RECIPE.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk());
    resultActions.andDo(document("likeController/count/{recipeId}",
        getDocumentRequest(),
        getDocumentResponse(),
        responseFields(
            fieldWithPath("count").type(JsonFieldType.NUMBER).description("좋아요 갯수 반환")
        )));

  }
}