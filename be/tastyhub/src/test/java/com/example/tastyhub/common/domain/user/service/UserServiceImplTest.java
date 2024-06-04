package com.example.tastyhub.common.domain.user.service;

import static com.example.tastyhub.fixture.user.UserFixture.CHANGE_PASSWORD_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.DUPLICATED_NICK_NAME;
import static com.example.tastyhub.fixture.user.UserFixture.DUPLICATED_USER_NAME;
import static com.example.tastyhub.fixture.user.UserFixture.FIND_ID_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.LOGIN_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.SEARCH_USER_DTO;
import static com.example.tastyhub.fixture.user.UserFixture.SIGNUP_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static com.example.tastyhub.fixture.user.UserFixture.USER_DELETE_REQUEST;
import static com.example.tastyhub.fixture.user.UserFixture.USER_DTO_LIST;
import static com.example.tastyhub.fixture.user.UserFixture.USER_UPDATE_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tastyhub.common.domain.user.dtos.SearchUserDto;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.user.repository.UserRepository;
import com.example.tastyhub.common.utils.Jwt.JwtUtil;
import com.example.tastyhub.common.utils.Redis.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RedisUtil redisUtil;

    @Mock
    JwtUtil jwtUtill;

    @Mock
    User user;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("username 중복 x")
    void checkDuplicatedUsername() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        userService.checkDuplicatedUsername("username");
        verify(userRepository, times(1)).existsByUsername(any());
    }

//    @Test
//    @DisplayName("username 중복 o")
//    void failCheckDuplicatedUserName() {
//        when(userRepository.existsByUsername(any())).thenReturn(true);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.checkDuplicatedUsername("username");
//        });
//
//        assertEquals("이미 존재하는 username입니다", exception.getMessage());
//        verify(userRepository, times(1)).existsByUsername(any());
//    }

    @Test
    @DisplayName("nickname 중복 x")
    void checkDuplicatedNickname() {
        when(userRepository.existsByNickname(any())).thenReturn(false);
        userService.checkDuplicatedNickname(USER.getNickname());
        verify(userRepository, times(1)).existsByNickname(any());

    }

    @Test
    @DisplayName("nickname 중복 o")
    void failCheckDuplicatedNickname() {
        when(userRepository.existsByNickname(any())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.checkDuplicatedNickname(USER.getNickname());
        });

        assertEquals("이미 존재하는 nickname입니다", exception.getMessage());
        verify(userRepository, times(1)).existsByNickname(any());
    }

//    @Test
//    @DisplayName("signup 성공")
//    void signup() throws IOException {
//        userService.signup(SIGNUP_REQUEST,any());
//        verify(userRepository, times(1)).save(any());
//    }

    @Test
    @DisplayName("login 성공")
    void login() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(userRepository.findByUsername(any())).willReturn(Optional.ofNullable(USER));
        userService.login(LOGIN_REQUEST, response);
        verify(userRepository, times(1)).findByUsername(LOGIN_REQUEST.getUsername());
        verify(jwtUtill, times(1)).createAccessToken(any(), any());
        verify(redisUtil, times(1)).setDataExpire(any(), any(), anyLong());
        verify(jwtUtill, times(1)).createRefreshToken(any(), any());

    }

    @Test
    @DisplayName("login 실패")
    void failLogin() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(userRepository.findByUsername(any())).willReturn(Optional.ofNullable(USER));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(LOGIN_REQUEST, response);
        });

        assertEquals("비밀번호가 일치하지않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("아이디 찾기")
    void findId() {
        given(userRepository.findByEmail(FIND_ID_REQUEST.getEmail())).willReturn(
            Optional.ofNullable(USER));
        userService.findId(FIND_ID_REQUEST);
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("아이디 찾기 실패")
    void findIdFail() {

        given(userRepository.findByEmail(any())).willThrow(
            new IllegalArgumentException("해당 회원은 존재하지 않습니다."));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findId(FIND_ID_REQUEST);
        });

        assertEquals("해당 회원은 존재하지 않습니다.", exception.getMessage());

    }
//
//    @Test
//    @DisplayName("비밀번호 변경하기")
//    void changePassword() {
//        given(userRepository.findByUsername(user.getUsername())).willReturn(
//            Optional.ofNullable(USER));
//        given(passwordEncoder.matches(any(), any())).willReturn(Boolean.TRUE);
//
//        userService.changePassword(CHANGE_PASSWORD_REQUEST, user);
//        verify(userRepository, times(1)).findByUsername(any());
////        verify(user, times(1)).updatePassword(CHANGE_PASSWORD_REQUEST.getChangePassword());
//
//    }

    @Test
    @DisplayName("비밀번호 변경하기 실패")
    void changePasswordFail() {

        given(userRepository.findByUsername(any())).willThrow(new IllegalArgumentException("비밀번호가 일치하지않습니다."));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(CHANGE_PASSWORD_REQUEST, USER);
        });

        assertEquals("비밀번호가 일치하지않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("사용자 리스트 반환하기")
    void getUserList() {
        given(userRepository.findAllByNickname(any())).willReturn(USER_DTO_LIST);
        userService.getUserList(USER.getNickname());
        verify(userRepository, times(1)).findAllByNickname(any());
    }

    @Test
    @DisplayName("사용자 리스트 반환하기 실패")
    void getUserListFail() {
//        given(userRepository.findAllByNickname(any())).willReturn(Collections.EMPTY_LIST);
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.getUserList(SEARCH_USER_DTO);
//        });
//        assertEquals("해당 닉네임을 가진 사용자가 없습니다.", exception.getMessage());
    }

//    @Test
//    @DisplayName("유저 삭제 성공")
//    void deleteUser() throws IOException {
//        given(passwordEncoder.matches(any(), any())).willReturn(Boolean.TRUE);
//        userService.delete(USER_DELETE_REQUEST, USER);
//        verify(userRepository, times(1)).delete(any());
//    }

    @Test
    @DisplayName("유저 삭제 실패")
    void deleteUserFail() {
        given(passwordEncoder.matches(any(), any())).willThrow(
            new IllegalArgumentException("비밀번호가 일치하지않습니다."));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.delete(USER_DELETE_REQUEST, USER);
        });

        assertEquals("비밀번호가 일치하지않습니다.", exception.getMessage());
    }

//    @Test
//    @DisplayName("사용자 정보 업데이트")
//    void updateUserInfo() throws IOException {
//        given(userRepository.findByUsername(any())).willReturn(Optional.ofNullable(USER));
//        userService.updateUserInfo(USER_UPDATE_REQUEST,any(),USER);
//        verify(userRepository, times(1)).findByUsername(any());
//    }

}