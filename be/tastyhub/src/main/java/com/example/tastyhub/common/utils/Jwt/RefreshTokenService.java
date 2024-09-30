package com.example.tastyhub.common.utils.Jwt;

import com.example.tastyhub.common.utils.Redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

  private final RedisUtil redisUtil;
  private static final long REFRESH_TOKEN_TIME =
      60 * 60 * 60 * 1000L; // Refresh token expiration time

  // Method to create a refresh token using the username
  public String createRefreshToken(String userName) {
    String keyOfRefreshToken = "RefreshToken:"+userName;
    try {
      String refreshToken = UUID.randomUUID().toString();
      redisUtil.saveData(keyOfRefreshToken,refreshToken,REFRESH_TOKEN_TIME);
      return refreshToken;
    } catch (Exception e) {
      // Error handling during token creation
      log.error("Error creating refresh token for user: " + userName, e);
      throw new RefreshTokenException("Failed to create refresh token", e,
          RefreshTokenException.ErrorType.CREATION_FAILED);
    }
  }

  // Method to update the refresh token
  public String updateRefreshToken(String userName) {
    String keyOfRefreshToken = "RefreshToken:"+userName;
    try {
      String newRefreshToken = UUID.randomUUID().toString();
      redisUtil.deleteData(keyOfRefreshToken);
      redisUtil.saveData(keyOfRefreshToken, newRefreshToken, REFRESH_TOKEN_TIME);
      return newRefreshToken;
    } catch (Exception e) {
      // Error handling during token update
      log.error("Error updating refresh token for user: " + userName, e);
      throw new RefreshTokenException("Failed to update refresh token", e,
          RefreshTokenException.ErrorType.UPDATE_FAILED);
    }
  }

  // Method to delete the refresh token
  public void deleteRefreshToken(String userName) {
    String keyOfRefreshToken = "RefreshToken:"+userName;
    try {
      redisUtil.deleteData(keyOfRefreshToken);
    } catch (Exception e) {
      // Error handling during token deletion
      log.error("Error deleting refresh token for user: " + userName, e);
      throw new RefreshTokenException("Failed to delete refresh token", e,
          RefreshTokenException.ErrorType.DELETION_FAILED);
    }
  }

  // Method to validate the refresh token
  public boolean validateRefreshToken(String userName, String refreshToken) {
    String keyOfRefreshToken = "RefreshToken:"+userName;
    try {
      if (!redisUtil.existData(keyOfRefreshToken)) {
        log.error("RefreshToken is fired: " + userName);
        return false;
      }

      String storedToken = redisUtil.getData(keyOfRefreshToken);

      if (storedToken == null || !storedToken.equals(refreshToken)) {
        log.error("RefreshToken is incorrect: " + userName);
        return false;
      }

      return true;
    } catch (Exception e) {
      // Error handling during token validation
      log.error("Error validating refresh token for user: " + userName, e);
      throw new RefreshTokenException("Failed to validate refresh token", e,
          RefreshTokenException.ErrorType.VALIDATION_FAILED);
    }
  }
}
