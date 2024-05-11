package com.example.tastyhub.common.verified.email.service;

import static com.example.tastyhub.fixture.email.EmailFixture.AUTH_EMAIL_REQUEST;
import static com.example.tastyhub.fixture.email.EmailFixture.VERIFIED_EMAIL_REQUEST;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.tastyhub.common.domain.user.repository.UserRepository;
import com.example.tastyhub.common.utils.Redis.RedisUtil;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    UserRepository userRepository;

    @Mock
    RedisUtil redisUtil;

    @InjectMocks
    EmailServiceImpl emailService;

    @Test
    @DisplayName("이메일 전송 성공")
    void authEmail() {
        emailService.authEmail(AUTH_EMAIL_REQUEST);
        verify(userRepository, times(1)).existsByEmail(any());
        verify(redisUtil, times(1)).existData(AUTH_EMAIL_REQUEST.getEmail());
        verify(javaMailSender, times(1)).send((MimeMessage) any());
    }

    @Test
    @DisplayName("이메일 검증 성공")
    void verifiedEmail() {
        emailService.verifiedEmail(VERIFIED_EMAIL_REQUEST);
        verify(redisUtil, times(1)).getData(any());
    }


}