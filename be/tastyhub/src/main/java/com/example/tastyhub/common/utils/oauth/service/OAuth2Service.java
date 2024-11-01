package com.example.tastyhub.common.utils.oauth.service;

import com.example.tastyhub.common.domain.user.dtos.NicknameDto;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface OAuth2Service {

  NicknameDto login(Map<String, Object> attributes, HttpServletResponse response);

  ResponseEntity<?> accessToken(String code);

//  void getUserInfo(String accessToken, HttpServletResponse response);

  ResponseEntity<?> getUserInfo(String accessToken);
}
