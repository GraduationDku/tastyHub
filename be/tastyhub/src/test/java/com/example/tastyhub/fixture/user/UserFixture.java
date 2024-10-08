package com.example.tastyhub.fixture.user;

import static com.example.tastyhub.fixture.village.VillageFixture.VILLAGE;

import com.example.tastyhub.common.domain.user.dtos.ChangePasswordRequest;
import com.example.tastyhub.common.domain.user.dtos.FindIdRequest;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.dtos.UserAuthRequest;

import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.user.dtos.UserNameDto;
import com.example.tastyhub.common.domain.user.dtos.NicknameDto;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.user.entity.User.userType;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

public class UserFixture {


  public static final SignupRequest SIGNUP_REQUEST =
      SignupRequest.builder()
          .userName("username")
          .password("password")
          .email("email01@gmail.com")
          .nickname("nickname")
          .build();

  public static final UserAuthRequest LOGIN_REQUEST =
      UserAuthRequest.builder()
          .userName("username")
          .password("password")
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


  public static final User USER =
      User.builder()
          .id(1L)
          .username("username")
          .password("password")
          .userType(userType.COMMON)
          .userImg("uri")
          .email("email@gmail.com")
          .nickname("nickname")
          .village(VILLAGE)
          .build();

 public static Pageable pageable = PageRequest.of(0, 1);


  public static final Page<UserDto> USER_DTO_LIST =
      new PageImpl(List.of(USER_DTO),pageable, 1);


  public static final UserAuthRequest USER_AUTH_REQUEST =
      UserAuthRequest.builder()
          .userName("username")
          .password("password")
          .build();


  public static final NicknameDto NICKNAME_DTO =
      new NicknameDto("nickname");

  public static final MockMultipartFile USER_IMAGE = new MockMultipartFile(
      "file",
      "user.jpg",
      "image/jpeg",
      "Test image content".getBytes()
  );



  public static final UserNameDto USER_NAME_RESPONSE = UserNameDto.builder()
      .userName(USER.getUsername()).build();

}
