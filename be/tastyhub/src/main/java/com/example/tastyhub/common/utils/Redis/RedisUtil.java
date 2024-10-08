package com.example.tastyhub.common.utils.Redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisUtil {

  private final StringRedisTemplate cacheRedisTemplate;  // 포트 6380 캐시용
  private final StringRedisTemplate verifiedRedisTemplate;  // 포트 6379 토큰/이메일 검증용

  // 쌍을 저장하는 메서드, SQL과 달리 만료시간 지정 가능
  public void saveData(String key, String value, long expiration) {
    ValueOperations<String, String> valueOperations = verifiedRedisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(expiration);
    valueOperations.set(key, value, expireDuration);
  }
  public String getData(String key) {
    ValueOperations<String, String> valueOperations = verifiedRedisTemplate.opsForValue();
    return valueOperations.get(key);
  }
  public boolean existData(String key) {
    return Boolean.TRUE.equals(verifiedRedisTemplate.hasKey(key));
  }

  public void deleteData(String key) {
    verifiedRedisTemplate.delete(key);
  }

  public void saveDataForCache(String key, String value, long expiration) {
    ValueOperations<String, String> valueOperations = cacheRedisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(expiration);
    valueOperations.set(key, value, expireDuration);
  }
  public String getDataForCache(String key) {
    ValueOperations<String, String> valueOperations = cacheRedisTemplate.opsForValue();
    return valueOperations.get(key);
  }
  public boolean existDataForCache(String key) {
    return Boolean.TRUE.equals(cacheRedisTemplate.hasKey(key));
  }

  public void deleteDataForCache(String key) {
    cacheRedisTemplate.delete(key);
  }
}
