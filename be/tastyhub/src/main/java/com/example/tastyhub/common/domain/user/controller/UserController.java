package com.example.tastyhub.common.domain.user.controller;

import static com.example.tastyhub.common.config.APIConfig.USER_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import com.example.tastyhub.common.domain.user.dtos.ChangePasswordRequest;
import com.example.tastyhub.common.domain.user.dtos.FindIdRequest;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.NicknameResponseDto;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDeleteRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.user.dtos.UserNameResponse;
import com.example.tastyhub.common.domain.user.dtos.UserUpdateRequest;
import com.example.tastyhub.common.domain.user.service.UserService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(USER_API)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/overlap/nickname")
    public ResponseEntity<StatusResponse> checkDuplicatedNickname(
            @RequestParam String nickname) {
        userService.checkDuplicatedNickname(nickname);
        return RESPONSE_OK;
    }

    @GetMapping("/overlap/username")
    public ResponseEntity<StatusResponse> checkDuplicatedUsername(
            @RequestParam String username) {
        userService.checkDuplicatedUsername(username);
        return RESPONSE_OK;
    }

    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signup(@RequestPart(value = "img", required = false) MultipartFile img, @RequestPart("data") SignupRequest signupRequest) throws IOException {
        userService.signup(signupRequest, img);
        return RESPONSE_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<NicknameResponseDto> login(@RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        NicknameResponseDto nickname = userService.login(loginRequest, response);
        return ResponseEntity.ok().body(nickname);
    }

    /**
     * 아이디 찾기 - skyriv213
     */
    @PostMapping("/findid")
    public ResponseEntity<UserNameResponse> findId(@RequestBody FindIdRequest findIdRequest) {
        UserNameResponse username = userService.findId(findIdRequest);
        return ResponseEntity.ok().body(username);
    }

    /**
     * 비밀번호 재설정 - skyriv213
     */
    @PatchMapping("/reset/password")
    public ResponseEntity<StatusResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.changePassword(changePasswordRequest, userDetails.getUser());
        return RESPONSE_OK;
    }

    /**
     * 사용자 검색하기 - skyriv213
     */

    @GetMapping("/search-list")
    public ResponseEntity<List<UserDto>> getUserList(@RequestParam String nickname) {
        List<UserDto> userDtoList = userService.getUserList(nickname);
        return ResponseEntity.ok().body(userDtoList);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<StatusResponse> delete(@RequestBody UserDeleteRequest deleteRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        userService.delete(deleteRequest, userDetails.getUser());
        return RESPONSE_OK;

    }

    @PatchMapping("/modify/information")
    public ResponseEntity<StatusResponse> updateUserInfo(@RequestPart("img") MultipartFile img, @RequestPart("data") UserUpdateRequest userUpdateRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{
            userService.updateUserInfoByUserUpdateRequest(userUpdateRequest, img, userDetails.getUser());
            return RESPONSE_OK;
        }

}
