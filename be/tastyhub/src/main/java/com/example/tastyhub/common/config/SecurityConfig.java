package com.example.tastyhub.common.config;

import static com.example.tastyhub.common.utils.Jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.example.tastyhub.common.utils.Jwt.JwtUtil.REFRESH_HEADER;

import java.util.ArrayList;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.tastyhub.common.utils.Jwt.JwtAuthFilter;
import com.example.tastyhub.common.utils.Jwt.JwtUtil;
import com.example.tastyhub.common.utils.Jwt.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] permitAllArray = {
        "/email",
        "/email/verified",
        "/user/overlap/nickname",
        "/user/overlap/username",
        "/user/login",
        "/user/signup",
        "/user/find/id",
        "/user/search/list/**",
        "/recipe/list",
        "/recipe/popular",
        "/recipe/search/",
        "/recipe/search/{keyword}",
        "/like/count/{recipeId}",
        "ws/chat",
        "ws/chat/**"

    };

    private final String[] permitOrigin = {
        "http://localhost:8080",
        "https://apic.app",
        "http://localhost:3000",
        "https://localhost:3000",
        "http://13.209.238.65"
    };


    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                .requestMatchers(permitAllArray).permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter.class);

//    http.formLogin().loginPage("/users/login");

        return http.build();
    }
}