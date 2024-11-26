package com.example.tastyhub.common.domain.scrap.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.scrap.ScrapFixture.PAGING_SCRAP_RESPONSES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.recipeReview.controller.RecipeReviewController;
import com.example.tastyhub.common.domain.recipeReview.service.RecipeReviewService;
import com.example.tastyhub.common.domain.scrap.service.ScrapService;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = ScrapController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class ScrapControllerTest {



  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  SetHttpHeaders setHttpHeaders;

  @MockBean
  ScrapService scrapService;

//  @Test
//  @WithCustomMockUser
//  void scrap() throws Exception {
//    // Given
//    Long recipeId =RECIPE.getId(); // 테스트할 recipeId
//
//    // ScrapService의 scrap 메서드를 Mock으로 처리
//    doNothing().when(scrapService).scrap(any(Long.class), any());
//
//    // When
//    ResultActions resultActions = mockMvc.perform(post("/scrap/{recipeId}", recipeId)
//        .accept("application/json")
//        .contentType("application/json"));
//
//    // Then
//    resultActions.andExpect(status().isOk()) // 응답이 200 OK인지 확인
//        .andDo(document("scrap/recipeId",
//            getDocumentRequest(),
//            getDocumentResponse(),
//            pathParameters(
//                parameterWithName("recipeId").description("The ID of the recipe to be scrapped")
//            )
//        ));
//
//    // ScrapService의 scrap 메서드가 호출되었는지 확인
//    verify(scrapService, times(1)).scrap(eq(recipeId), any());
//  }


  @Test
  @WithCustomMockUser
  void getScrapList() throws Exception {
    // Mock 설정
    when(scrapService.getScrapList(any(), any())).thenReturn(PAGING_SCRAP_RESPONSES);

    // API 호출 테스트
    // API 호출 테스트
    ResultActions resultActions = mockMvc.perform(get("/scrap/list")
            .accept("application/json"))
        .andExpect(status().isOk());


    // RestDocs 문서화
    resultActions.andDo(document("scrap/get-list",
        getDocumentRequest(),
        getDocumentResponse(),
        responseFields(
            fieldWithPath("content[].foodId").description("The ID of the scrap"),
            fieldWithPath("content[].foodName").description("The name of the food in the scrap"),
            fieldWithPath("content[].foodImgUrl").description("The image URL of the food"),
            fieldWithPath("number").description("The current page number"),
            fieldWithPath("size").description("The size of the page"),
            fieldWithPath("last").description("Is this the last page"),
            fieldWithPath("totalPages").description("The total number of pages"),
            fieldWithPath("totalElements").description("The total number of elements"),
            fieldWithPath("first").description("Is this the first page"),
            fieldWithPath("sort.empty").description("Is the sort empty"),
            fieldWithPath("sort.unsorted").description("Is the sort unsorted"),
            fieldWithPath("sort.sorted").description("Is the sort sorted"),
            fieldWithPath("numberOfElements").description("The number of elements on the current page"),
            fieldWithPath("empty").description("Is the current page empty"),
            // Add pageable documentation
            fieldWithPath("pageable.pageNumber").description("The current page number"),
            fieldWithPath("pageable.pageSize").description("The size of the page"),
            fieldWithPath("pageable.offset").description("The offset of the current page"),
            fieldWithPath("pageable.paged").description("Is pagination enabled"),
            fieldWithPath("pageable.unpaged").description("Is pagination disabled"),
            fieldWithPath("pageable.sort.empty").description("Is the sort empty"),
            fieldWithPath("pageable.sort.sorted").description("Is the sort sorted"),
            fieldWithPath("pageable.sort.unsorted").description("Is the sort unsorted")
        )
    ));
  }

}