package com.example.tastyhub.common.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.tastyhub.common.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("username 중복 x")
    void checkDuplicatedUsername() {

    }

    @Test
    @DisplayName("username 중복 o")
    void failCheckDuplicatedUserName() {

    }

    @Test
    @DisplayName("nickname 중복 x")
    void checkDuplicatedNickname() {

    }

    @Test
    @DisplayName("nickname 중복 o")
    void failCheckDuplicatedNickname() {

    }

    @Test
    @DisplayName("signup 성공")
    void signup() {
    }

    @Test
    @DisplayName("signup 실패")
    void failSignup() {
    }

    @Test
    @DisplayName("login 성공")
    void login() {
    }

    @Test
    @DisplayName("login 실패")
    void failLogin() {
    }
}