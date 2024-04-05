package com.example.tastyhub.common.verified.email.dtos;

import lombok.Getter;

@Getter
public class VerifiedEmailRequest {

    private String email;
    private String verifiedCode;

}
