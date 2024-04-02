package com.example.tastyhub.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8080") //허용 출처
            .allowedMethods("GET", "POST", "PATCH", "OPTIONS", "DELETE", "HEAD")//허용 메소드
            .allowCredentials(true)// 쿠키 인증 허용
            .maxAge(3000);//pre-flight 캐싱 기간
    }
    
    
    
}
