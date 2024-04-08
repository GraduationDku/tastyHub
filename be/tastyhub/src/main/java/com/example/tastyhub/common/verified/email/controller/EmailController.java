package com.example.tastyhub.common.verified.email.controller;

import static com.example.tastyhub.common.config.APIConfig.EMAIL_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.verified.email.dtos.AuthEmailRequest;
import com.example.tastyhub.common.verified.email.dtos.VerifiedEmailRequest;
import com.example.tastyhub.common.verified.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EMAIL_API)
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<StatusResponse> AuthEmail(AuthEmailRequest authEmail) {
        emailService.authEmail(authEmail);
        return RESPONSE_OK;
    }

    @PostMapping("/verified")
    public ResponseEntity<Boolean> verifiedEmail(VerifiedEmailRequest verifiedEmailRequest) {
        boolean value = emailService.verifiedEmail(verifiedEmailRequest);
        return ResponseEntity.ok().body(value);
    }
}
