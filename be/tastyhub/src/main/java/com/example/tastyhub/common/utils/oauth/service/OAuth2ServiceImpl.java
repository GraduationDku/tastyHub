package com.example.tastyhub.common.utils.oauth.service;

import static com.example.tastyhub.common.domain.user.service.UserServiceImpl.REFRESH_TOKEN_TIME;
import static com.example.tastyhub.common.utils.Jwt.JwtService.AUTHORIZATION_HEADER;
import static com.example.tastyhub.common.utils.Jwt.JwtService.REFRESH_HEADER;

import com.example.tastyhub.common.domain.user.dtos.NicknameDto;
import com.example.tastyhub.common.domain.user.entity.User.userType;
import com.example.tastyhub.common.utils.Jwt.JwtService;
import com.example.tastyhub.common.utils.Redis.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service{

  private final JwtService jwtService;
  private final RedisUtil redisUtil;

  @Override
  public NicknameDto login(Map<String, Object> attributes, HttpServletResponse response) {



    String accessToken = jwtService.createAccessToken((String) attributes.get("name"),
        userType.COMMON);
    String refreshToken = jwtService.createRefreshToken((String) attributes.get("name"),
        userType.COMMON);

    redisUtil.setDataExpire(REFRESH_HEADER, refreshToken, REFRESH_TOKEN_TIME);
    response.addHeader(AUTHORIZATION_HEADER, accessToken);
    response.addHeader(REFRESH_HEADER, refreshToken);
    return new NicknameDto((String) attributes.get("name")+String.valueOf((Math.random()*1000)+1));

  }
}
