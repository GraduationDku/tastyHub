package com.example.tastyhub.common.domain.recipe.controller;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.post.controller.PostController;
import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.POST_API;
import static com.example.tastyhub.common.config.APIConfig.RECIPE_API;
import static com.example.tastyhub.fixture.post.PostFixture.POST_CREATE_REQUEST;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.PAGING_RECIPE_RESPONSE_PAGE;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE_CREATE_DTO;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE_DTO;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE_UPDATE_DTO;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = RecipeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class RecipeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  RecipeService recipeService;

  @MockBean
  SetHttpHeaders setHttpHeaders;


  @Test
  @WithMockUser
  @DisplayName("인기 레시피 조회하기")
  void getPopuralRecipes() throws Exception {

    when(recipeService.getPopularRecipes(any())).thenReturn(PAGING_RECIPE_RESPONSE_PAGE);

    ResultActions resultActions = mockMvc.perform(get(RECIPE_API + "/popular")

            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("recipeController/popular",
            getDocumentResponse(),

            responseFields(
                fieldWithPath("content[].foodId").type(JsonFieldType.NUMBER).description("레시피 음식 ID"),
                fieldWithPath("content[].foodName").type(JsonFieldType.STRING).description("레시피 이름"),
                fieldWithPath("content[].foodImgUrl").type(JsonFieldType.STRING)
                    .description("레시피 이미지 URL"),
                fieldWithPath("content[].foodInformationDto.foodInformationId").type(
                    JsonFieldType.NUMBER).description("음식 정보 ID"),
                fieldWithPath("content[].foodInformationDto.text").type(JsonFieldType.STRING)
                    .description("음식 정보 설명"),
                fieldWithPath("content[].foodInformationDto.cookingTime").type(JsonFieldType.NUMBER)
                    .description("음식 조리 시간"),
                fieldWithPath("content[].foodInformationDto.serving").type(JsonFieldType.STRING)
                    .description("음식 조리 시 기준 인원"),
                fieldWithPath("pageable").ignored(),
                fieldWithPath("last").description("Indicates if this is the last page"),
                fieldWithPath("totalElements").description("Total number of elements"),
                fieldWithPath("totalPages").description("Total number of pages"),
                fieldWithPath("first").description("Indicates if this is the first page"),
                fieldWithPath("size").description("The size of the page"),
                fieldWithPath("number").description("The number of the current page"),
                fieldWithPath("sort.empty").description("Sort empty flag"),
                fieldWithPath("sort.sorted").description("Sort sorted flag"),
                fieldWithPath("sort.unsorted").description("Sort unsorted flag"),
                fieldWithPath("numberOfElements").description("Number of elements in the current page"),
                fieldWithPath("empty").description("Indicates if the page is empty")
            )
        )
    );
  }

  @Test
  @WithMockUser
  @DisplayName("모든 레시피 조회하기")
  void getAllRecipes() throws Exception {

    when(recipeService.getAllRecipes(any())).thenReturn(PAGING_RECIPE_RESPONSE_PAGE);

    ResultActions resultActions = mockMvc.perform(get(RECIPE_API + "/list")
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("recipeController/list",
            getDocumentResponse(),
            responseFields(
                fieldWithPath("content[].foodId").type(JsonFieldType.NUMBER).description("레시피 음식 ID"),
                fieldWithPath("content[].foodName").type(JsonFieldType.STRING).description("레시피 이름"),
                fieldWithPath("content[].foodImgUrl").type(JsonFieldType.STRING)
                    .description("레시피 이미지 URL"),
                fieldWithPath("content[].foodInformationDto.foodInformationId").type(
                    JsonFieldType.NUMBER).description("음식 정보 ID"),
                fieldWithPath("content[].foodInformationDto.text").type(JsonFieldType.STRING)
                    .description("음식 정보 설명"),
                fieldWithPath("content[].foodInformationDto.cookingTime").type(JsonFieldType.NUMBER)
                    .description("음식 조리 시간"),
                fieldWithPath("content[].foodInformationDto.serving").type(JsonFieldType.STRING)
                    .description("음식 조리 시 기준 인원"),
                fieldWithPath("pageable").ignored(),
                fieldWithPath("last").description("Indicates if this is the last page"),
                fieldWithPath("totalElements").description("Total number of elements"),
                fieldWithPath("totalPages").description("Total number of pages"),
                fieldWithPath("first").description("Indicates if this is the first page"),
                fieldWithPath("size").description("The size of the page"),
                fieldWithPath("number").description("The number of the current page"),
                fieldWithPath("sort.empty").description("Sort empty flag"),
                fieldWithPath("sort.sorted").description("Sort sorted flag"),
                fieldWithPath("sort.unsorted").description("Sort unsorted flag"),
                fieldWithPath("numberOfElements").description("Number of elements in the current page"),
                fieldWithPath("empty").description("Indicates if the page is empty")
            )
        )
    );
  }


  @Test
  @WithMockUser
  @DisplayName("키워드로 레시피 검색")
  void getSearchedRecipes() throws Exception {

    when(recipeService.getSearchedRecipes(any(), any())).thenReturn(PAGING_RECIPE_RESPONSE_PAGE);

    ResultActions resultActions = mockMvc.perform(
            get(RECIPE_API + "/search/{keyword}", RECIPE.getFoodName())

                .contentType(MediaType.APPLICATION_JSON)

//                .param("nickname", USER.getNickname())  // URL 파라미터 추가
                .content(objectMapper.writeValueAsString(RECIPE_CREATE_DTO))

                .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("recipeController/search",
            getDocumentResponse(),
            responseFields(
                fieldWithPath("content[].foodId").type(JsonFieldType.NUMBER).description("레시피 음식 ID"),
                fieldWithPath("content[].foodName").type(JsonFieldType.STRING).description("레시피 이름"),
                fieldWithPath("content[].foodImgUrl").type(JsonFieldType.STRING)
                    .description("레시피 이미지 URL"),
                fieldWithPath("content[].foodInformationDto.foodInformationId").type(
                    JsonFieldType.NUMBER).description("음식 정보 ID"),
                fieldWithPath("content[].foodInformationDto.text").type(JsonFieldType.STRING)
                    .description("음식 정보 설명"),
                fieldWithPath("content[].foodInformationDto.cookingTime").type(JsonFieldType.NUMBER)
                    .description("음식 조리 시간"),
                fieldWithPath("content[].foodInformationDto.serving").type(JsonFieldType.STRING)
                    .description("음식 조리 시 기준 인원"),
                fieldWithPath("pageable").ignored(),
                fieldWithPath("last").description("Indicates if this is the last page"),
                fieldWithPath("totalElements").description("Total number of elements"),
                fieldWithPath("totalPages").description("Total number of pages"),
                fieldWithPath("first").description("Indicates if this is the first page"),
                fieldWithPath("size").description("The size of the page"),
                fieldWithPath("number").description("The number of the current page"),
                fieldWithPath("sort.empty").description("Sort empty flag"),
                fieldWithPath("sort.sorted").description("Sort sorted flag"),
                fieldWithPath("sort.unsorted").description("Sort unsorted flag"),
                fieldWithPath("numberOfElements").description("Number of elements in the current page"),
                fieldWithPath("empty").description("Indicates if the page is empty")
            )
        )
    );

  }
//
//  @Test
//  @WithCustomMockUser
//  @DisplayName("레시피 생성")
//  void createRecipe() throws Exception {
//
//    doNothing().when(recipeService).createRecipe(any(), any(),any());
//
//    ResultActions resultActions = mockMvc.perform(post(RECIPE_API + "/create")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(RECIPE_CREATE_DTO))
//            .with(csrf()))
//        .andExpect(status().isOk());
//
//    resultActions.andDo(document("recipeController/create",
//        getDocumentRequest(),
//        requestFields(
//            fieldWithPath("foodName").type(JsonFieldType.STRING)
//                .description("음식 이름"),
//            fieldWithPath("foodImg").type(JsonFieldType.STRING)
//                .description("음식 사진"),
//            fieldWithPath("foodInformation").type(JsonFieldType.OBJECT)
//                .description("음식 정보 객체"),
//            fieldWithPath("foodInformation.text").type(JsonFieldType.STRING)
//                .description("음식 정보 내용"),
//            fieldWithPath("foodInformation.cookingTime").type(JsonFieldType.NUMBER)
//                .description("조리 시간"),
//            fieldWithPath("foodInformation.serving").type(JsonFieldType.STRING)
//                .description("조리 기준 식사량"),
//            fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("재료 리스트"),
//            fieldWithPath("ingredients[].ingredientName").type(JsonFieldType.STRING)
//                .description("재료 이름"),
//            fieldWithPath("ingredients[].amount").type(JsonFieldType.STRING).description("재료 량"),
//            fieldWithPath("cookSteps").type(JsonFieldType.ARRAY).description("조리 단계"),
//            fieldWithPath("cookSteps[].stepNumber").type(JsonFieldType.NUMBER).description("조리 순서"),
//            fieldWithPath("cookSteps[].stepImg").type(JsonFieldType.STRING).description(
//                "조리 이미지"),
//            fieldWithPath("cookSteps[].text").type(JsonFieldType.STRING).description("조리 단계 설명"))));
//  }
//

  @Test
  @WithMockUser
  @DisplayName("레시피 정보 가져오기")
  void getRecipe() throws Exception {

    when(recipeService.getRecipe(any())).thenReturn(RECIPE_DTO);

    ResultActions resultActions = mockMvc.perform(
            get(RECIPE_API + "/detail/{recipeId}", RECIPE.getId())
                .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("recipeController/detail",
            getDocumentResponse(),
            responseFields(
                fieldWithPath("foodId").type(JsonFieldType.NUMBER).description("음식 ID"),
                fieldWithPath("foodName").type(JsonFieldType.STRING).description("음식 이름"),
                fieldWithPath("foodImgUrl").type(JsonFieldType.STRING).description("음식 이미지 URL"),
                fieldWithPath("foodInformation").type(JsonFieldType.OBJECT).description("음식 정보 객체"),
                fieldWithPath("foodInformation.foodInformationId").type(JsonFieldType.NUMBER)
                    .description("음식 정보 객체 ID"),
                fieldWithPath("foodInformation.text").type(JsonFieldType.STRING)
                    .description("음식 정보 내용"),
                fieldWithPath("foodInformation.cookingTime").type(JsonFieldType.NUMBER)
                    .description("음식 정보 시간"),
                fieldWithPath("foodInformation.serving").type(JsonFieldType.STRING)
                    .description("기준 식사량"),
                fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description("재료 리스트"),
                fieldWithPath("ingredients[].ingredientId").type(JsonFieldType.NUMBER)
                    .description("재료 식별자"),
                fieldWithPath("ingredients[].ingredientName").type(JsonFieldType.STRING)
                    .description("재료 이름"),
                fieldWithPath("ingredients[].amount").type(JsonFieldType.STRING).description("재료 량"),
                fieldWithPath("cookSteps").type(JsonFieldType.ARRAY).description("조리과정 리스트"),
                fieldWithPath("cookSteps[].cookStepId").type(JsonFieldType.NUMBER)
                    .description("조리과정 식별자"),
                fieldWithPath("cookSteps[].stepNumber").type(JsonFieldType.NUMBER)
                    .description("조리과정 순서"),
                fieldWithPath("cookSteps[].stepImgUrl").type(JsonFieldType.STRING)
                    .description("조리과정 이미지"),
                fieldWithPath("cookSteps[].text").type(JsonFieldType.STRING).description("조리과정 설명"),
                // 페이징 관련 필드 추가
                fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("Paging information")
                    .optional(),
                fieldWithPath("last").type(JsonFieldType.OBJECT)
                    .description("Indicator if this is the last page").optional(),
                fieldWithPath("totalElements").type(JsonFieldType.OBJECT)
                    .description("Total number of elements").optional(),
                fieldWithPath("totalPages").type(JsonFieldType.OBJECT)
                    .description("Total number of pages").optional(),
                fieldWithPath("first").type(JsonFieldType.OBJECT)
                    .description("Indicator if this is the first page").optional(),
                fieldWithPath("size").type(JsonFieldType.OBJECT).description("Size of the page")
                    .optional(),
                fieldWithPath("number").type(JsonFieldType.OBJECT).description("Current page number")
                    .optional(),
                fieldWithPath("sort.empty").type(JsonFieldType.OBJECT).description("Sort empty")
                    .optional(),
                fieldWithPath("sort.sorted").type(JsonFieldType.OBJECT).description("Sort sorted")
                    .optional(),
                fieldWithPath("sort.unsorted").type(JsonFieldType.OBJECT).description("Sort unsorted")
                    .optional(),
                fieldWithPath("numberOfElements").type(JsonFieldType.OBJECT)
                    .description("Number of elements in the current page").optional(),
                fieldWithPath("empty").type(JsonFieldType.OBJECT)
                    .description("Indicator if the page is empty").optional()
            )
        )
    );
  }
//
//  @Test
//  @WithCustomMockUser
//  @DisplayName("레시피 업데이트")
//  void updateRecipe() throws Exception {
//
//    doNothing().when(recipeService).updateRecipe(any(), any(), any(),any());
//
//    ResultActions resultActions = mockMvc.perform(
//            patch(RECIPE_API + "/modify/{recipeId}", RECIPE.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(RECIPE_UPDATE_DTO))
//                .with(csrf()))
//        .andExpect(status().isOk());
//
//    resultActions.andDo(document("recipeController/modify",
//            getDocumentRequest(),
//            getDocumentResponse(),
//            requestFields(
//                fieldWithPath("foodName").type(JsonFieldType.STRING).description(""),
//                fieldWithPath("foodImg").type(JsonFieldType.STRING).description(""),
//                fieldWithPath("foodInformation").type(JsonFieldType.OBJECT).description(""),
//                fieldWithPath("foodInformation.text").type(JsonFieldType.STRING).description(""),
//                fieldWithPath("foodInformation.cookingTime").type(JsonFieldType.NUMBER).description(""),
//                fieldWithPath("foodInformation.serving").type(JsonFieldType.STRING).description(""),
//                fieldWithPath("ingredients").type(JsonFieldType.ARRAY).description(""),
//                fieldWithPath("ingredients[].ingredientName").type(JsonFieldType.STRING)
//                    .description(""),
//                fieldWithPath("ingredients[].amount").type(JsonFieldType.STRING).description(""),
//                fieldWithPath("cookSteps").type(JsonFieldType.ARRAY).description(""),
//                fieldWithPath("cookSteps[].stepNumber").type(JsonFieldType.NUMBER).description(""),
//                fieldWithPath("cookSteps[].stepImg").type(JsonFieldType.STRING).description(""),
//                fieldWithPath("cookSteps[].text").type(JsonFieldType.STRING).description(""),
//                fieldWithPath("foodInformation.foodInformationId").type(JsonFieldType.NUMBER)
//                    .description(""),
//                fieldWithPath("ingredients[].ingredientId").type(JsonFieldType.NUMBER).description(""),
//                fieldWithPath("cookSteps[].cookStepId").type(JsonFieldType.NUMBER).description("")
//            ),
//            responseFields(
//                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
//                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
//            )
//        )
//    );
//
//  }

  @Test
  @WithCustomMockUser
  @DisplayName("레시피 삭제하기")
  void deleteRecipe() throws Exception {


    doNothing().when(recipeService).deleteRecipe(any(), any());

    ResultActions resultActions = mockMvc.perform(
            delete(RECIPE_API + "/{recipeId}", RECIPE.getId())

                .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("recipeController/delete",
            getDocumentResponse(),

            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
            )
        )
    );
  }
}