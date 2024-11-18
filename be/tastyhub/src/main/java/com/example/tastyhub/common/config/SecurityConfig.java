package com.example.tastyhub.common.config;

import com.example.tastyhub.common.utils.oauth.service.OAuth2Service;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.tastyhub.common.utils.Jwt.JwtAuthFilter;
import com.example.tastyhub.common.utils.Jwt.AccessTokenService;
import com.example.tastyhub.common.utils.Jwt.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//  private final OAuth2Service oAuth2UserService;
  private final AccessTokenService accessTokenService;
  private final UserDetailsServiceImpl userDetailsService;
  private final String[] permitAllArray = {
      "/email",
      "/email/verified",
      "/user/overlap/nickname",
      "/user/overlap/username",
      "/user/login",
      "/user/refresh",
      "/user/signup",
      "/recipe/list",
      "/recipe/popular",
      "/recipe/search/",
      "/recipe/search/{keyword}",
      "/like/count/{recipeId}",
      "/ws/chat",
      "/ws/chat/**",
      "/oauth2/**"
  };

  private final String[] permitOrigin = {
      "http://localhost:8080",
      "https://apic.app",
      "http://localhost:3000",
      "http://13.209.238.65",
      "https://localhost:3000",
      "https://www.googleapis.com/oauth2/**"
  };

  CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedOrigins(List.of(permitOrigin));
      config.setAllowedOriginPatterns(List.of(permitAllArray));
      config.setAllowCredentials(true);
      config.setExposedHeaders(Arrays.asList("Authorization", "Refresh"));
      return config;
    };
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/", "/login").permitAll()
            .requestMatchers(permitAllArray).permitAll()
            .anyRequest().authenticated()
        );

    http.addFilterBefore(jwtAuthFilter(),
        UsernamePasswordAuthenticationFilter.class);


    return http.build();
  }

//  @Bean
//  public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
//    DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
//    return request -> {
//      OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(request);
//      return oAuth2User;
//    };
//  }

  @Bean // Jwt 유효성 검증 필터
  public JwtAuthFilter jwtAuthFilter() {
    return new JwtAuthFilter(accessTokenService, userDetailsService);
  }

  @Bean  // Password Encryption
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
