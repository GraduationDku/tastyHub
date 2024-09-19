package com.example.tastyhub.common.utils.oauth.controller;

import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.oauth.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

  private final OAuth2Service oAuth2Service;

  @GetMapping("/home")
  public ResponseEntity<StatusResponse> home(Principal principal, HttpServletResponse response) {

    OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) principal;

    Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();

    oAuth2Service.login(attributes, response);

    return RESPONSE_OK;
  }
}
