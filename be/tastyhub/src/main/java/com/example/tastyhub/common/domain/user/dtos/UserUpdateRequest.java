package com.example.tastyhub.common.domain.user.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserUpdateRequest {
    private String nickname;
}
