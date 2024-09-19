package com.example.tastyhub.common.domain.user.service;

import com.example.tastyhub.common.domain.user.dtos.ChangePasswordRequest;
import com.example.tastyhub.common.domain.user.dtos.UserAuthRequest;

import com.example.tastyhub.common.domain.user.dtos.FindIdRequest;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.user.dtos.UserNameResponse;
import com.example.tastyhub.common.domain.user.dtos.NicknameDto;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.village.dtos.LocationRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void signup(SignupRequest signupRequest, MultipartFile img) throws IOException;

    void checkDuplicatedNickname(String duplicatedNickName);

    NicknameDto login(UserAuthRequest loginRequest, HttpServletResponse response);

    void checkDuplicatedUsername(String duplicatedName);

    UserNameResponse findId(FindIdRequest findIdRequest);

    void changePassword(ChangePasswordRequest changePasswordRequest, User user);

    Page<UserDto> getUserList(String searchUserDto, Pageable pageable);

    void delete(UserAuthRequest deleteRequest, User user) throws IOException;

    void updateUserInfoByUserUpdateRequest(NicknameDto nicknameDto, MultipartFile img, User user) throws IOException;

    void setVillage(LocationRequest locationRequest, String username);
}
