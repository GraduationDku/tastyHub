package com.example.tastyhub.common.domain.user.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.USER_API;
import static com.example.tastyhub.fixture.user.UserFixture.CHANGE_PASSWORD_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.NICKNAME_DTO;
import static com.example.tastyhub.fixture.user.UserFixture.SIGNUP_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static com.example.tastyhub.fixture.user.UserFixture.USER_AUTH_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.USER_DTO_LIST;
import static com.example.tastyhub.fixture.user.UserFixture.pageable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.dtos.NicknameDto;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.user.service.UserService;
import com.example.tastyhub.common.utils.Jwt.AccessTokenService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  UserService userService;

  @MockBean
  SetHttpHeaders setHttpHeaders;

  @MockBean
  AccessTokenService accessTokenService;


  @Test
  @WithMockUser
  @DisplayName("사용자 닉네임 중복 체크")
  void checkDuplicatedNickname() throws Exception {
    doNothing().when(userService).checkDuplicatedNickname(USER.getNickname());

    ResultActions resultActions = mockMvc.perform(get(USER_API + "/overlap/nickname")
            .param("nickname", USER.getNickname())  // URL 파라미터 추가
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("userController/overlap/nickname",
        getDocumentRequest(),
        getDocumentResponse(),
        queryParameters(  // requestFields 대신 requestParameters 사용
            parameterWithName("nickname").description("닉네임"),
            parameterWithName("_csrf").ignored() // _csrf 매개변수 무시
        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }

  @Test
  @WithMockUser
  @DisplayName("사용자 아이디 중복 체크")
  void checkDuplicatedUsername() throws Exception {
    doNothing().when(userService).checkDuplicatedUsername(USER.getUsername());

    ResultActions resultActions = mockMvc.perform(get(USER_API + "/overlap/username")
            .param("username", USER.getUsername())  // URL 파라미터 추가
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("userController/overlap/username",
        getDocumentRequest(),
        getDocumentResponse(),
        queryParameters(  // requestFields 대신 requestParameters 사용
            parameterWithName("username").description("사용자 이름"),
            parameterWithName("_csrf").ignored() // _csrf 매개변수 무시

        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }

  @Test
  @WithMockUser
  @DisplayName("회원가입 하기")
  void signup() throws Exception {
    // 이미지 파일 파트
    MockMultipartFile imgFile = new MockMultipartFile(
        "img",
        "test.png",
        "image/png",
        "test image content".getBytes()
    );

    // JSON 데이터 파트
    MockMultipartFile dataFile = new MockMultipartFile(
        "data",
        "",
        "application/json",
        objectMapper.writeValueAsString(SIGNUP_REQUEST).getBytes()
    );

    // 서비스 메서드의 Mock 설정
    doNothing().when(userService).signup(any(SignupRequest.class), any(MultipartFile.class));

    // MockMvc를 사용하여 멀티파트 요청 수행
    ResultActions resultActions = mockMvc.perform(multipart(USER_API + "/signup")
            .file(imgFile) // 이미지 파일 추가
            .file(dataFile) // JSON 데이터 추가
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(csrf()))
        .andExpect(status().isCreated());

    // REST Docs 문서화
    resultActions.andDo(document("userController/signup",
        getDocumentRequest(),
        getDocumentResponse(),
        requestParts(
            partWithName("img").description("프로필 이미지 파일"),
            partWithName("data").description("회원 가입 데이터 (JSON)")
        ),
        requestPartFields("data",
            fieldWithPath("userName").type(JsonFieldType.STRING).description("유저 이름"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }


  @Test
  @WithMockUser
  @DisplayName("로그인 하기")
  void login() throws Exception {

    given(userService.login(any(), any())).willReturn(NICKNAME_DTO);

    ResultActions resultActions = mockMvc.perform(post(USER_API + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(USER_AUTH_REQUEST))
            .with(csrf()))
        .andExpect(status().isOk())
//        .andExpect((ResultMatcher) jsonPath("$.nickname").value("nickname")); // 응답 데이터 검증
        ;
    resultActions.andDo(document("userController/login",
        getDocumentRequest(),
        getDocumentResponse(),
        requestFields(
            fieldWithPath("userName").type(JsonFieldType.STRING).description("유저 이름"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
        ),
        responseFields(
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임")
        )
    ));

  }
//
//  @Test
//  @WithMockUser
//  @DisplayName("사용자 아이디 찾기")
//  void findId() throws Exception {
////        doNothing().when(userService).findId(FIND_ID_REQUEST);
////        when(userService.findId(FIND_ID_REQUEST)).thenReturn(USER.getUsername());
//
//    ResultActions resultActions = mockMvc.perform(post(USER_API + "/find/id")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(FIND_ID_REQUEST))
//            .with(csrf()))
//        .andExpect(status().isOk());
//
//    resultActions.andDo(document("userController/find/id",
//        getDocumentRequest(),
//        getDocumentResponse(),
//        requestFields(
//            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
//        )
//    ));
//
//  }

  @Test
  @WithCustomMockUser
  @DisplayName("사용자 비밀번호 변경하기")
  void changePassword() throws Exception {
    doNothing().when(userService).changePassword(any(), any());

    ResultActions resultActions = mockMvc.perform(patch(USER_API + "/reset/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(CHANGE_PASSWORD_REQUEST))
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("userController/reset/password",
        getDocumentRequest(),
        getDocumentResponse(),
        requestFields(
            fieldWithPath("beforePassword").type(JsonFieldType.STRING).description("수정 전 비밀번호"),
            fieldWithPath("changePassword").type(JsonFieldType.STRING).description("수정 후 비밀번호")
        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }

  @Test
  @WithCustomMockUser
  @DisplayName("사용자 리스트 조회")
  void getUserList() throws Exception {

    given(userService.getUserList(any(), any())).willReturn(USER_DTO_LIST);


    ResultActions resultActions = mockMvc.perform(get(USER_API + "/search/list")
            .param("nickname", USER.getNickname())  // URL 파라미터 추가
            .with(csrf()))
        .andExpect(status().isOk());

    resultActions.andDo(document("userController/search/list",
        getDocumentRequest(),
        getDocumentResponse(),
        queryParameters(  // requestFields 대신 requestParameters 사용
            parameterWithName("nickname").description("닉네임"),
            parameterWithName("_csrf").ignored() // _csrf 매개변수 무시
        ),
        responseFields(
            fieldWithPath("content[].userId").type(JsonFieldType.NUMBER).description("사용자 Id"),
            fieldWithPath("content[].nickname").type(JsonFieldType.STRING).description("닉네임"),
            fieldWithPath("content[].userImg").type(JsonFieldType.STRING).description("사용자 이미지"),
            fieldWithPath("pageable.pageNumber").description("현재 페이지 번호입니다. 0부터 시작합니다."),
            fieldWithPath("pageable.pageSize").description("페이지 크기, 즉 페이지당 항목 수입니다."),
            fieldWithPath("pageable.offset").description("해당 페이지의 시작 위치(계산: pageNumber * pageSize)입니다."),
            fieldWithPath("pageable.paged").description("응답이 페이지 처리되어 있는지 여부를 나타냅니다."),
            fieldWithPath("pageable.unpaged").description("페이지네이션이 비활성화되어 있는지 여부를 나타냅니다."),
            fieldWithPath("pageable.sort.empty").description("정렬 조건이 비어 있는지 여부를 나타냅니다."),
            fieldWithPath("pageable.sort.sorted").description("결과가 정렬되어 있는지 여부를 나타냅니다."),
            fieldWithPath("pageable.sort.unsorted").description("결과가 정렬되지 않았는지 여부를 나타냅니다."),
            fieldWithPath("content").description("현재 페이지의 내용(실제 데이터)입니다."),
            fieldWithPath("totalElements").description("모든 페이지에서의 총 요소 수입니다."),
            fieldWithPath("totalPages").description("사용 가능한 전체 페이지 수입니다."),
            fieldWithPath("last").description("이 페이지가 마지막 페이지인지 여부를 나타냅니다."),
            fieldWithPath("first").description("이 페이지가 첫 번째 페이지인지 여부를 나타냅니다."),
            fieldWithPath("numberOfElements").description("현재 페이지의 요소 수입니다."),
            fieldWithPath("size").description("페이지당 요소 수입니다."),
            fieldWithPath("number").description("현재 페이지 번호입니다."),
            fieldWithPath("sort.empty").description("정렬이 적용되었는지 여부를 나타냅니다(이 예시에서는 항상 true)."),
            fieldWithPath("sort.sorted").description("결과가 정렬되었는지 여부를 나타냅니다."),
            fieldWithPath("sort.unsorted").description("결과가 정렬되지 않았는지 여부를 나타냅니다."),
            fieldWithPath("empty").description("비어있는지 아닌지 확인.")

        )

        )
    );
  }

  @Test
  @WithCustomMockUser
  @DisplayName("사용자 삭제하기")
  void delete() throws Exception {

    doNothing().when(userService).delete(eq(USER_AUTH_REQUEST), any());

    ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.delete(USER_API + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(USER_AUTH_REQUEST))
                .with(csrf()))
        .andExpect(status().is2xxSuccessful());

    resultActions.andDo(document("userController/delete",
        getDocumentRequest(),
        getDocumentResponse(),
        requestFields(
            fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자 이름"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));

  }

  @Test
  @WithCustomMockUser
  @DisplayName("사용자 정보 업데이트")
  void updateUserInfo() throws Exception {

    // 이미지 파일 파트
    MockMultipartFile imgFile = new MockMultipartFile(
        "img",
        "test.png",
        "image/png",
        "test image content".getBytes()
    );

    // JSON 데이터 파트
    MockMultipartFile dataFile = new MockMultipartFile(
        "data",
        "",
        "application/json",
        objectMapper.writeValueAsString(NICKNAME_DTO).getBytes()
    );

    // 서비스 메서드의 Mock 설정
    doNothing().when(userService)
        .updateUserInfoByUserUpdateRequest(any(NicknameDto.class), any(MultipartFile.class), any(
            User.class));

    // MockMvc를 사용하여 멀티파트 요청 수행
    ResultActions resultActions = mockMvc.perform(multipart(USER_API + "/modify/information")
            .file(imgFile) // 이미지 파일 추가
            .file(dataFile) // JSON 데이터 추가
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(csrf())
            .with(request -> {
              request.setMethod("PATCH"); // HTTP 메서드를 PATCH로 설정
              return request;
            }))
        .andExpect(status().isOk());

    // REST Docs 문서화
    resultActions.andDo(document("userController/modify/information",
        getDocumentRequest(),
        getDocumentResponse(),
        requestParts(
            partWithName("img").description("프로필 이미지 파일"),
            partWithName("data").description("회원 가입 데이터 (JSON)")
        ),
        requestPartFields("data",
//            fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
//            fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
//            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
        ),
        responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 반환 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지")
        )
    ));
  }
}