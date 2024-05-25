package com.example.tastyhub.common.domain.user.controller;

import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.tastyhub.asciidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.tastyhub.common.config.APIConfig.USER_API;
import static com.example.tastyhub.fixture.user.UserFixture.SIGNUP_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tastyhub.annotation.WithCustomMockUser;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.service.UserService;
import com.example.tastyhub.common.utils.Jwt.JwtUtil;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    JwtUtil jwtUtil;




//    @Test
//    void checkDuplicatedNickname() {
//    }
//
//    @Test
//    void checkDuplicatedUsername() {
//    }

    @Test
    @WithMockUser
    void signup() throws Exception {
        doNothing().when(userService).signup(SIGNUP_REQUEST);

        ResultActions resultActions = mockMvc.perform(post(USER_API+"/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(SIGNUP_REQUEST))
                .with(csrf()))
            .andExpect(status().isOk());

        resultActions.andDo(document("userController/signup",
            getDocumentRequest(),
            getDocumentResponse(),
            requestFields(
                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
            )
        ));

    }
}
//
//    @Test
//    void login() {
//    }
//
//    @Test
//    void findId() {
//    }
//
//    @Test
//    void changePassword() {
//    }
//
//    @Test
//    void getUserList() {
//    }
//
//    @Test
//    void delete() {
//    }
//
//    @Test
//    void updateUserInfo() {
//    }
//}