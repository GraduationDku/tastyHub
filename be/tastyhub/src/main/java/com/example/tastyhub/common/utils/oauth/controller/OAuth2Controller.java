package com.example.tastyhub.common.utils.oauth.controller;

import static com.example.tastyhub.common.config.APIConfig.OAUTH;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.oauth.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(OAUTH)
public class OAuth2Controller {
//
//  private final OAuth2Service oAuth2Service;
//
//  @PostMapping("/google")
//  public ResponseEntity<StatusResponse> GoogleLogin(
////      Principal principal, HttpServletResponse response
//      @RequestParam String tokenId, HttpServletResponse response
//  ) {
//
//    ResponseEntity<?> responseEntity = oAuth2Service.accessToken(tokenId);
//    System.out.println(tokenId);
//
////    oAuth2Service.getUserInfo(accessToken,response);
//
//    return RESPONSE_OK;
//  }
}
