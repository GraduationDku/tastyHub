package com.example.tastyhub.fixture.email;

import com.example.tastyhub.common.verified.email.dtos.AuthEmailRequest;
import com.example.tastyhub.common.verified.email.dtos.VerifiedEmailRequest;

public class EmailFixture {

    public static AuthEmailRequest AUTH_EMAIL_REQUEST =
        new AuthEmailRequest("Auth@email.com");

    public static VerifiedEmailRequest VERIFIED_EMAIL_REQUEST =
        VerifiedEmailRequest.builder()
            .email("Auth@email.com")
            .verifiedCode("123456")
            .build();

}
