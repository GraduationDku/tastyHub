package com.example.tastyhub.common.domain.user.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAuthRequest {

    private String userName;
    private String password;

}
