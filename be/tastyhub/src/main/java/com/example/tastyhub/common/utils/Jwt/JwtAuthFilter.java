package com.example.tastyhub.common.utils.Jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tastyhub.common.dto.StatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsService;
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String accessToken = jwtService.resolveAccessToken(request);
      if (!jwtService.validateToken(accessToken)) {
        throw new JwtAuthException("JWT Authentication error!");
      }
      Claims info = jwtService.getUserInfoFromToken(accessToken);
      String name = info.getSubject();
      String role = info.get("auth").toString();
      setAuthentication(name, role);
    } catch (JwtAuthException | UsernameNotFoundException exception) {
      log.error("JwtAuthentication Authentication Exception Occurs! - {}", exception.getClass());
    }

    filterChain.doFilter(request, response);

  }

  public Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());
  }

  public void setAuthentication(String username, String role) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = this.createAuthentication(username);
//    if (role.equals("Admin")) {
//      authentication = this.createAdminAuthentication(username);
//    } else {
//      authentication = this.createAuthentication(username);
//    }
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }


}
