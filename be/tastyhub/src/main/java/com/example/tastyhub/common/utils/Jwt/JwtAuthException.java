package com.example.tastyhub.common.utils.Jwt;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthException extends AuthenticationException {
  public JwtAuthException(String msg) {
    super(msg);
  }
}
