package com.example.tastyhub.common.verified.email.service;

import com.example.tastyhub.common.verified.email.dtos.AuthEmailRequest;
import com.example.tastyhub.common.verified.email.dtos.VerifiedEmailRequest;

public interface EmailService  {

    void authEmail(AuthEmailRequest authEmail);

    boolean verifiedEmail(VerifiedEmailRequest verifiedEmailRequest);
}
