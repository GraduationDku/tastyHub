package com.example.tastyhub.common.domain.user.service;

import com.example.tastyhub.common.domain.user.dtos.ChangePasswordRequest;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedNickName;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedUserName;
import com.example.tastyhub.common.domain.user.dtos.FindIdRequest;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.SearchUserDto;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    void signup(SignupRequest signupRequest);

    void checkDuplicatedNickname(DuplicatedNickName duplicatedNickName);

    void login(LoginRequest loginRequest, HttpServletResponse response);

    void checkDuplicatedUsername(DuplicatedUserName duplicatedName);

    String findId(FindIdRequest findIdRequest);

    void changePassword(ChangePasswordRequest changePasswordRequest, User user);

    List<UserDto> getUserList(SearchUserDto searchUserDto);
}
