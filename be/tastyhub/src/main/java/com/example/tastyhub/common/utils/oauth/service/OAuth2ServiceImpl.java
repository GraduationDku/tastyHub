package com.example.tastyhub.common.utils.oauth.service;

import static com.example.tastyhub.common.utils.Jwt.AccessTokenService.AUTHORIZATION_HEADER;

import com.example.tastyhub.common.domain.user.dtos.NicknameDto;
import com.example.tastyhub.common.domain.user.entity.User.userType;
import com.example.tastyhub.common.utils.Jwt.AccessTokenService;
import com.example.tastyhub.common.utils.Jwt.RefreshTokenService;
import com.example.tastyhub.common.utils.Redis.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service{

  private final AccessTokenService accessTokenService;
  private final RefreshTokenService refreshTokenService;

  @Override
  public NicknameDto login(Map<String, Object> attributes, HttpServletResponse response) {



    String accessToken = accessTokenService.createAccessToken((String) attributes.get("name"),
        userType.COMMON);
    String refreshToken = refreshTokenService.createRefreshToken((String) attributes.get("name"));
    // AccessToken 응답 헤더에 저장
    response.addHeader(AUTHORIZATION_HEADER, accessToken);

    // Refresh Token 쿠키에 저장
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    refreshCookie.setHttpOnly(true); // JavaScript를 통한 접근 방지
    refreshCookie.setSecure(true); // HTTPS를 통해서만 쿠키 전송
    refreshCookie.setPath("/"); // 사이트 전체에서 쿠키 사용
    refreshCookie.setMaxAge(14 * 24 * 60 * 60); // 2주
    response.addCookie(refreshCookie);

    return new NicknameDto((String) attributes.get("name")+String.valueOf((Math.random()*1000)+1));

  }
}
