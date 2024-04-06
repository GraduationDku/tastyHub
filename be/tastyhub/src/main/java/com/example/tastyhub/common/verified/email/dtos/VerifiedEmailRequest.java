package com.example.tastyhub.common.verified.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifiedEmailRequest {

    private String email;
    private String verifiedCode;

}
