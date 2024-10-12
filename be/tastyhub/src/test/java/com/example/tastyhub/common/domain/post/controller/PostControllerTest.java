package com.example.tastyhub.common.domain.post.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.POST_API;
import static com.example.tastyhub.fixture.post.PostFixture.PAGING_POST_RESPONSES;
import static com.example.tastyhub.fixture.post.PostFixture.POST;
import static com.example.tastyhub.fixture.post.PostFixture.POST_CREATE_REQUEST;
import static com.example.tastyhub.fixture.post.PostFixture.POST_RESPONSE;
import static com.example.tastyhub.fixture.post.PostFixture.POST_UPDATE_REQUEST;
import static com.example.tastyhub.fixture.post.PostFixture.pageable;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.post.service.PostService;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class PostControllerTest {


  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PostService postService;


  @Test
  @WithCustomMockUser
  void createPost() throws Exception {

    doNothing().when(postService).createPost(POST_CREATE_REQUEST, USER);

    ResultActions resultActions = mockMvc.perform(post(POST_API + "/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(POST_CREATE_REQUEST))
            .with(csrf()))
        .andExpect(status().isCreated());

    resultActions.andDo(document("postController/create",
        getDocumentRequest(),
        getDocumentResponse(),
        requestFields(
            fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }

  @Test
  @WithCustomMockUser
  void updatePost() throws Exception {

    doNothing().when(postService).updatePost(POST.getId(), POST_UPDATE_REQUEST, USER);

    ResultActions resultActions = mockMvc.perform(patch(POST_API + "/modify/{postId}", POST.getId())
            .contentType(MediaType.APPLICATION_JSON)
//                .param("nickname", USER.getNickname())  // URL 파라미터 추가
            .content(objectMapper.writeValueAsString(POST_UPDATE_REQUEST))
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("postController/modify",
        getDocumentRequest(),
        getDocumentResponse(),
//            queryParameters(  // requestFields 대신 requestParameters 사용
//                parameterWithName("postId").description("게시글 아이디"),
//                parameterWithName("_csrf").ignored() // _csrf 매개변수 무시
//            ),

        requestFields(
            fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
            fieldWithPath("postState").type(JsonFieldType.STRING).description("게시글 상태")
        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }

  @Test
  @WithCustomMockUser
  void deletePost() throws Exception {
    doNothing().when(postService).deletePost(POST.getId(), USER);

    ResultActions resultActions = mockMvc.perform(
            delete(POST_API + "/delete/{postId}", POST.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("nickname", USER.getNickname())  // URL 파라미터 추가
//                .content(objectMapper.writeValueAsString(POST_UPDATE_REQUEST))
                .with(csrf()))
        .andExpect(status().is2xxSuccessful());

    resultActions.andDo(document("postController/delete",
        getDocumentRequest(),
        getDocumentResponse(),
//            queryParameters(  // requestFields 대신 requestParameters 사용
//                parameterWithName("postId").description("게시글 아이디"),
//                parameterWithName("_csrf").ignored() // _csrf 매개변수 무시
//            ),

//            requestFields(
//                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
//                fieldWithPath).t("text"ype(JsonFieldType.STRING).description("게시글 내용")
//            ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }

  @Test
  @WithCustomMockUser(username = "testUser", role = User.userType.COMMON)  // 사용자 인증 설정
  public void getAllPost() throws Exception {
    // Mocking: postService의 getAllPost 메서드가 PAGING_POST_RESPONSES를 반환하도록 설정
    given(postService.getAllPost(any(), any()))
        .willReturn(PAGING_POST_RESPONSES);

    // GET 요청 실행
    ResultActions resultActions = mockMvc.perform(get("/post/list")
            .param("page", "0")
            .param("size", "10")
            .param("sort", "createdAt,DESC")
            .with(csrf()))  // CSRF 토큰 추가
        .andExpect(status().isOk())  // HTTP 상태 코드 200 확인
        .andDo(MockMvcResultHandlers.print());  // 요청 및 응답 출력

    // 응답 본문을 디버깅 용도로 출력
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("Response Body: " + responseBody);

    // REST Docs 문서화
    resultActions.andDo(document("postController/list",
        queryParameters(
            parameterWithName("page").description("조회할 페이지 번호"),
            parameterWithName("size").description("페이지 당 게시글 수"),
            parameterWithName("sort").description("정렬 기준과 방향"),
            parameterWithName("_csrf").description("CSRF 토큰")
        ),
        responseFields(
            fieldWithPath("content[].postId").type(JsonFieldType.NUMBER).description("게시글 ID"),
            fieldWithPath("content[].postState").type(JsonFieldType.STRING).description("게시글 상태"),
            fieldWithPath("content[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
            fieldWithPath("content[].userImg").type(JsonFieldType.STRING).description("작성자 이미지"),
            fieldWithPath("content[].title").type(JsonFieldType.STRING).description("게시글 제목"),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 게시글 수"),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 게시글 수"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비었는지 여부"),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지에서 반환된 게시글 수"),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이지가 비어 있는지 여부")
        )
    ));
  }

  @Test
  @WithCustomMockUser
  void getAllRecentPost() throws Exception {
    given(postService.getAllRecentPost(any(),any())).willReturn(PAGING_POST_RESPONSES);

    ResultActions resultActions = mockMvc.perform(get(POST_API + "/recent/list")
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("postController/recent/list",
        getDocumentRequest(),
        getDocumentResponse(),
        responseFields(
            fieldWithPath("content[].postId").type(JsonFieldType.NUMBER).description("게시글 ID"),
            fieldWithPath("content[].postState").type(JsonFieldType.STRING).description("게시글 상태"),
            fieldWithPath("content[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
            fieldWithPath("content[].userImg").type(JsonFieldType.STRING).description("작성자 이미지"),
            fieldWithPath("content[].title").type(JsonFieldType.STRING).description("게시글 제목"),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 게시글 수"),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 게시글 수"),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
            fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비었는지 여부"),
            fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않았는지 여부"),
            fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬되었는지 여부"),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지에서 반환된 게시글 수"),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이지가 비어 있는지 여부")
        )
    ));
  }

  @Test
  @WithMockUser
  void getPost() throws Exception {
    given(postService.getPost(any())).willReturn(POST_RESPONSE);

    ResultActions resultActions = mockMvc.perform(
            get(POST_API + "/detail/{postId}", POST.getId())
                .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("postController/detail",
        getDocumentRequest(),
        getDocumentResponse(),
        responseFields(
            fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 아이디"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
            fieldWithPath("postState").type(JsonFieldType.STRING).description("게시글 상태"),
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
            fieldWithPath("userImg").type(JsonFieldType.STRING).description("사용자 이미지"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
            fieldWithPath("latestUpdateTime").type(JsonFieldType.STRING).description("게시글 시간"),
            fieldWithPath("commentDtos").type(JsonFieldType.ARRAY).description("댓글 목록"),
            fieldWithPath("commentDtos[].nickname").type(JsonFieldType.STRING)
                .description("댓글 작성자 닉네임"),
            fieldWithPath("commentDtos[].userImg").type(JsonFieldType.STRING)
                .description("댓글 작성자 이미지"),
            fieldWithPath("commentDtos[].content").type(JsonFieldType.STRING).optional()
                .description("댓글 내용"),
            fieldWithPath("commentDtos[].state").type(JsonFieldType.BOOLEAN).description("댓글 상태"),
            fieldWithPath("commentDtos[].latestUpdateTime").type(JsonFieldType.STRING)
                .description("댓글 작성 시간")
        )
    ));

  }
}