package com.example.tastyhub.common.domain.comment.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.COMMENT_API;
import static com.example.tastyhub.common.config.APIConfig.POST_API;
import static com.example.tastyhub.fixture.comment.CommentFixture.COMMENT;
import static com.example.tastyhub.fixture.comment.CommentFixture.COMMENT_REQUEST;
import static com.example.tastyhub.fixture.post.PostFixture.POST;
import static com.example.tastyhub.fixture.post.PostFixture.POST_CREATE_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.comment.entity.Comment;
import com.example.tastyhub.common.domain.comment.service.CommentService;
import com.example.tastyhub.common.domain.post.controller.PostController;
import com.example.tastyhub.common.domain.post.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@WebMvcTest(controllers = CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class CommentControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;


//    @Test
//    @WithCustomMockUser
//    void createComment() throws Exception {
//
//        doNothing().when(commentService).createComment(eq(POST.getId()), eq(COMMENT_REQUEST), any());
//
//        ResultActions resultActions = mockMvc.perform(post(COMMENT_API + "/create/{postId}",POST.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(COMMENT_REQUEST))
//                .with(csrf()))
//            .andExpect(status().isCreated());
//
//        resultActions.andDo(document("commentController/create",
//            getDocumentRequest(),
//            getDocumentResponse(),
//            requestFields(
//                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
//            ),
////            queryParameters(  // requestFields 대신 requestParameters 사용
////                parameterWithName("nickname").description("닉네임"),
////                parameterWithName("_csrf").ignored() // _csrf 매개변수 무시
////            ),
//            responseFields(
//                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
//                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
//            )
//        ));
//
//    }
//
//    @Test
//    @WithCustomMockUser
//    void updateComment() throws Exception {
//
//        doNothing().when(commentService).updateComment(eq(COMMENT.getId()), eq(COMMENT_REQUEST), any());
//
//        ResultActions resultActions = mockMvc.perform(patch(COMMENT_API + "/modify/{commentId}",COMMENT.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(COMMENT_REQUEST))
//                .with(csrf()))
//            .andExpect(status().isOk());
//
//        resultActions.andDo(document("commentController/modify",
//            getDocumentRequest(),
//            getDocumentResponse(),
//            requestFields(
//                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
//            ),
////            queryParameters(  // requestFields 대신 requestParameters 사용
////                parameterWithName("nickname").description("닉네임"),
////                parameterWithName("_csrf").ignored() // _csrf 매개변수 무시
////            ),
//            responseFields(
//                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
//                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
//            )
//        ));
//    }
//
//    @Test
//    @WithCustomMockUser
//    void deleteComment() throws Exception {
//
//        doNothing().when(commentService).deleteComment(eq(COMMENT.getId()),  any());
//
//        ResultActions resultActions = mockMvc.perform(patch(COMMENT_API + "/delete/{commentId}",COMMENT.getId())
//                .with(csrf()))
//            .andExpect(status().is2xxSuccessful());
//
//        resultActions.andDo(document("commentController/delete",
//            getDocumentRequest(),
//            getDocumentResponse(),
////            queryParameters(  // requestFields 대신 requestParameters 사용
////                parameterWithName("nickname").description("닉네임"),
////                parameterWithName("_csrf").ignored() // _csrf 매개변수 무시
////            ),
//            responseFields(
//                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
//                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
//            )
//        ));
//    }
}