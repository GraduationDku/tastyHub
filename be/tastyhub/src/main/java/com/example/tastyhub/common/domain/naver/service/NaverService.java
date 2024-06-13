package com.example.tastyhub.common.domain.naver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverService {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${naver.client.id}")
  private String clientId;

  @Value("${naver.client.secret}")
  private String clientSecret;

  public String getAddressFromCoordinates(double latitude, double longitude) {
    String url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=" + longitude + "," + latitude + "&orders=roadaddr&output=json";

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
    headers.set("X-NCP-APIGW-API-KEY", clientSecret);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

      String address = "";
      JsonNode root = objectMapper.readTree(response.getBody());
      address = root.path("results").get(0).path("region").path("area3").path("name").asText();

      return address;

    } catch (HttpClientErrorException e) {
      System.err.println("HTTP 오류: " + e.getStatusCode());
      System.err.println("응답 본문: " + e.getResponseBodyAsString());
      e.printStackTrace();
      return "인증 실패. 인증 정보를 확인하세요.";
    } catch (Exception e) {
      e.printStackTrace();
      return "요청 처리 중 오류가 발생했습니다.";
    }
  }
}
