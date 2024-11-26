package com.example.tastyhub.common.domain.userReview.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.USERREVIEW_API;
import static com.example.tastyhub.fixture.userReview.UserReviewFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.config.APIConfig;
import com.example.tastyhub.common.domain.userReview.service.UserReviewService;
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
@WebMvcTest(controllers = UserReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class UserReviewControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  UserReviewService userReviewService;

  @Test
  @WithCustomMockUser
  void createUserReview() throws Exception {
    doNothing().when(userReviewService).createUserReview(eq(1L), eq(USER_REVIEW_CREATE_REQUEST), any());

    ResultActions resultActions = mockMvc.perform(post(USERREVIEW_API + "/create/{userId}", 1L)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(USER_REVIEW_CREATE_REQUEST))
            .with(csrf()))
        .andExpect(status().isCreated());

    resultActions.andDo(document("user-review/create",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("userId").description("The ID of the user")
        ),
        requestFields(
            fieldWithPath("grade").description("The grade of the review"),
            fieldWithPath("content").description("The content of the review")
        )
    ));
  }
  @Test
  @WithCustomMockUser
  void updateUserReview() throws Exception {
    doNothing().when(userReviewService).updateUserReviewByUserReviewUpdateRequest(eq(
        USER_REVIEW.getId()), eq(USER_REVIEW_UPDATE_REQUEST), any());

    ResultActions resultActions = mockMvc.perform(patch(USERREVIEW_API + "/modify/{reviewId}", 1L)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(USER_REVIEW_UPDATE_REQUEST))
            .with(csrf()))
        .andExpect(status().isOk()); // 수정됨

    resultActions.andDo(document("user-review/update",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("reviewId").description("The ID of the review")
        ),
        requestFields(
            fieldWithPath("grade").description("The updated grade of the review"),
            fieldWithPath("content").description("The updated content of the review")
        )
    ));
  }

  @Test
  @WithCustomMockUser
  void getUserReviews() throws Exception {
    when(userReviewService.getUserReviews(eq(1L), any())).thenReturn(PAGING_USER_REVIEW_RESPONSES);

    ResultActions resultActions = mockMvc.perform(get(USERREVIEW_API + "/list/{userId}", 1L)
            .accept("application/json"))
        .andExpect(status().isOk());

    resultActions.andDo(document("user-review/get-list",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("userId").description("The ID of the user")
        ),
        responseFields(
            fieldWithPath("content[].nickname").description("The nickname of the reviewer"),
            fieldWithPath("content[].grade").description("The grade of the review"),
            fieldWithPath("content[].content").description("The content of the review"),
            fieldWithPath("totalPages").description("The total number of pages"),
            fieldWithPath("totalElements").description("The total number of elements"),
            fieldWithPath("last").description("Is this the last page"),
            fieldWithPath("first").description("Is this the first page"),
            fieldWithPath("size").description("The size of the page"),
            fieldWithPath("number").description("The current page number"),
            fieldWithPath("numberOfElements").description("Number of elements on the current page"),
            fieldWithPath("empty").description("Is the current page empty"),
            fieldWithPath("sort.empty").description("Whether the sort is empty"),
            fieldWithPath("sort.unsorted").description("Whether the results are unsorted"),
            fieldWithPath("sort.sorted").description("Whether the results are sorted")
        )
    ));
  }



  @Test
  @WithCustomMockUser
  void deleteUserReview() throws Exception {
    doNothing().when(userReviewService).deleteUserReview(eq(1L), any());

    ResultActions resultActions = mockMvc.perform(delete(USERREVIEW_API + "/delete/{reviewId}", 1L)
            .with(csrf()))
        .andExpect(status().isNoContent());

    resultActions.andDo(document("user-review/delete",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("reviewId").description("The ID of the review")
        )
    ));
  }
}
