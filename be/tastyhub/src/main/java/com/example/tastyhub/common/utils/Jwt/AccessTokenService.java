package com.example.tastyhub.common.utils.Jwt;

import io.jsonwebtoken.io.Encoders;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.tastyhub.common.domain.user.entity.User.userType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessTokenService {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String REFRESH_HEADER = "Refresh";
  public static final String AUTHORIZATION_KEY = "auth";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final long ACCESS_TOKEN_TIME = 10 * 60 * 1000L;
  ;

  private static SecretKey makeKey() {
    return Keys.secretKeyFor(SignatureAlgorithm.HS512);
  }
  //    @Value("${jwt.secret.key}")

  private String secretKey = Encoders.BASE64.encode(makeKey().getEncoded());

  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

  //초기화
  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // username, userType를 가지고 AccessToken 을 생성하는 메서드
  public String createAccessToken(String username, userType role) {

    Date issuedAt = new Date();
    Date expireAt = new Date(issuedAt.getTime() + ACCESS_TOKEN_TIME);

    //payload에 넣어줄 정보
    Claims claims = Jwts.claims();
    claims.put(AUTHORIZATION_KEY, role); //권한

    return BEARER_PREFIX + Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(issuedAt) //발급 시기
        .setExpiration(expireAt) //만료 시기
        .signWith(key, signatureAlgorithm) // 사용할 암호화 알고리즘과 , signature 에 들어갈 key값 세팅
        .compact();
  }


  // 요청 header에서 AccessToken을 가져오는 메서드
  public String resolveAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 요청 header에서 RefeshToken을 가져오는 메서드

  // 토큰 유효성 체크하는 메서드
  public boolean validateAccessToken(String token) {
    if (token == null) {
      log.info("JWT is null, JWT 토큰이 null입니다.");
      return false;
    }

    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

  // 토큰에서 사용자 정보 가져오는 메서드
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  // Admin 권한 확인 메서드
  public boolean isAdminToken(String token) {
    Claims claims = getUserInfoFromToken(token);
    return (claims.get(AUTHORIZATION_KEY).toString()).equals("ADMIN");
  }


}
