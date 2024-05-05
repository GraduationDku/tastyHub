package com.example.tastyhub.common.domain.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {
    private String nickname;
}
