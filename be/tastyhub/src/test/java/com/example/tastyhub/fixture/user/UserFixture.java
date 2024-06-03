package com.example.tastyhub.fixture.user;

import static com.example.tastyhub.fixture.village.VillageFixture.VILLAGE;

import com.example.tastyhub.common.domain.user.dtos.ChangePasswordRequest;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedNickName;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedUserName;
import com.example.tastyhub.common.domain.user.dtos.FindIdRequest;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.SearchUserDto;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDeleteRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.user.dtos.UserUpdateRequest;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.user.entity.User.userType;
import java.util.Collections;
import java.util.List;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class UserFixture {

    public static final DuplicatedUserName DUPLICATED_USER_NAME =
        DuplicatedUserName.builder()
            .username("username")
            .build();

    public static final DuplicatedNickName DUPLICATED_NICK_NAME =
        DuplicatedNickName.builder()
            .nickname("nickname").build();

    public static final SignupRequest SIGNUP_REQUEST =
        SignupRequest.builder()
            .username("username")
            .password("password")
            .email("email01@gmail.com")
            .nickname("nickname")
            .build();

    public static final LoginRequest LOGIN_REQUEST =
        LoginRequest.builder()
            .username("username")
            .password("password")
            .build();

    public static final User USER =
        User.builder()
            .username("username")
            .password("password")
            .userType(userType.COMMON)
            .userImg(null)
            .email("email@gmail.com")
            .nickname("nickname")
            .village(VILLAGE)
            .build();


    public static final FindIdRequest FIND_ID_REQUEST =
        FindIdRequest.builder()
            .email("example@email.com")
            .build();

    public static final ChangePasswordRequest CHANGE_PASSWORD_REQUEST =
        ChangePasswordRequest.builder()
            .beforePassword("beforePassword")
            .changePassword("changePassword")
            .build();

    public static final UserDto USER_DTO =
        UserDto.builder()
            .userId(1L)
            .nickname("nickname")
            .userImg("uri")
            .build();

    public static final SearchUserDto SEARCH_USER_DTO =
        SearchUserDto.builder()
            .nickname("nickname")
            .build();


    public static final List<UserDto> USER_DTO_LIST =
        Collections.singletonList(USER_DTO);


    public static final UserDeleteRequest USER_DELETE_REQUEST =
        UserDeleteRequest.builder()
            .username("username")
            .password("password")
            .build();


    public static final UserUpdateRequest USER_UPDATE_REQUEST =
        new UserUpdateRequest("nickname");

    public static final MultipartFile USER_IMAGE = new MockMultipartFile(
            "file",
            "user.jpg",
            "image/jpeg",
            "Test image content".getBytes()
        );
}
