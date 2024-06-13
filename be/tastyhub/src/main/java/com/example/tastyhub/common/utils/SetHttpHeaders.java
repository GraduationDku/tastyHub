package com.example.tastyhub.common.utils;


import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class SetHttpHeaders {



    public HttpHeaders setHttpHeaderTypeJson() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        httpHeaders.setDate(new Date().getTime());
        return httpHeaders;
    }
}
