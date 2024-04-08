package com.example.tastyhub.common.domain.user.controller;


import static com.example.tastyhub.common.config.APIConfig.USER_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import com.example.tastyhub.common.domain.user.dtos.DuplicatedNickName;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedUserName;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.service.UserService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.SetHttpHeaders;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(USER_API)
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final SetHttpHeaders setHttpHeaders;

    @GetMapping("/overlap/nickname")
    public ResponseEntity<StatusResponse> checkDuplicatedNickname(
        @RequestBody DuplicatedNickName duplicatedNickName) {
        userService.checkDuplicatedNickname(duplicatedNickName);
        return RESPONSE_OK;
    }

    @GetMapping("/overlap/username")
    public ResponseEntity<StatusResponse> checkDuplicatedUsername(
        @RequestBody DuplicatedUserName duplicatedUserName) {
        userService.checkDuplicatedUsername(duplicatedUserName);
        return RESPONSE_OK;
    }

    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signup(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return RESPONSE_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<StatusResponse> login(@RequestBody LoginRequest loginRequest,
        HttpServletResponse response) {

        userService.login(loginRequest, response);
        return RESPONSE_OK;
    }
}
