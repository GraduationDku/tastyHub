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

    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    // JSON parsing logic here
    // For simplicity, let's assume the address is in the "formatted_address" field of the JSON response
    // You can use a library like Jackson or Gson to parse the response

    String address = "";
    try {
      JsonNode root = objectMapper.readTree(response.getBody());
      address = root.path("results").get(0).path("region").path("area1").path("name").asText();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return address;
  }
}
