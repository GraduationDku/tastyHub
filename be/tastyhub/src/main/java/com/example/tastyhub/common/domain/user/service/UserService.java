package com.example.tastyhub.common.domain.user.service;

import com.example.tastyhub.common.domain.user.dtos.DuplicatedNickName;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedUserName;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void signup(SignupRequest signupRequest);

    void checkDuplicatedNickname(DuplicatedNickName duplicatedNickName);

    void login(LoginRequest loginRequest, HttpServletResponse response);

    void checkDuplicatedUsername(DuplicatedUserName duplicatedName);
}
