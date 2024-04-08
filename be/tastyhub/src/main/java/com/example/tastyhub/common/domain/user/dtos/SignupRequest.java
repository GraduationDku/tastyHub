package com.example.tastyhub.common.domain.user.dtos;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder

public class SignupRequest {

    private String username;
    private String password;
    private String nickname;
    private String email;
    private MultipartFile userImg;

}
