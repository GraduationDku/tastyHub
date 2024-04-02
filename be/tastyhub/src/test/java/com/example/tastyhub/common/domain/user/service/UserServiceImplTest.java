package com.example.tastyhub.common.domain.user.service;

import static com.example.tastyhub.fixture.user.UserFixture.DUPLICATED_NICK_NAME;
import static com.example.tastyhub.fixture.user.UserFixture.DUPLICATED_USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tastyhub.common.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
    UserServiceImpl userService;

    @Test
    @DisplayName("username 중복 x")
    void checkDuplicatedUsername() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        userService.checkDuplicatedUsername(DUPLICATED_USER_NAME);
        verify(userRepository, times(1)).existsByUsername(any());
    }

    @Test
    @DisplayName("username 중복 o")
    void failCheckDuplicatedUserName() {
        when(userRepository.existsByUsername(any())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.checkDuplicatedUsername(DUPLICATED_USER_NAME);
        });

        assertEquals("이미 존재하는 username입니다", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(any());
    }

    @Test
    @DisplayName("nickname 중복 x")
    void checkDuplicatedNickname() {
        when(userRepository.existsByNickname(any())).thenReturn(false);
        userService.checkDuplicatedNickname(DUPLICATED_NICK_NAME);
        verify(userRepository, times(1)).existsByNickname(any());

    }

    @Test
    @DisplayName("nickname 중복 o")
    void failCheckDuplicatedNickname() {
        when(userRepository.existsByNickname(any())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.checkDuplicatedNickname(DUPLICATED_NICK_NAME);
        });

        assertEquals("이미 존재하는 nickname입니다", exception.getMessage());
        verify(userRepository, times(1)).existsByNickname(any());
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