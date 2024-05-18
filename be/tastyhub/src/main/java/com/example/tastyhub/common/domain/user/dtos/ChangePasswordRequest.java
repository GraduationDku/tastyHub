package com.example.tastyhub.common.domain.user.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePasswordRequest {

    private String beforePassword;
    private String changePassword;

}
