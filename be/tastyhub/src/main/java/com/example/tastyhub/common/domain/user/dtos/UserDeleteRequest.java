package com.example.tastyhub.common.domain.user.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDeleteRequest {

    private String username;
    private String password;

}
