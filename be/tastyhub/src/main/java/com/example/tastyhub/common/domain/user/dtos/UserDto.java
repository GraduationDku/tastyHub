package com.example.tastyhub.common.domain.user.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private String nickname;

    private String userImg;
}
