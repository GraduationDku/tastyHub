package com.example.tastyhub.common.utils.Redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisUtil {

  private final StringRedisTemplate template;

  // 쌍을 저장하는 메서드, SQL과 달리 만료시간 지정 가능
  public void saveData(String key, String value, long expiration) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    Duration expireDuration = Duration.ofSeconds(expiration);
    valueOperations.set(key, value, expireDuration);
  }
  public String getData(String key) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    return valueOperations.get(key);
  }
  public boolean existData(String key) {
    return Boolean.TRUE.equals(template.hasKey(key));
  }

  public void deleteData(String key) {
    template.delete(key);
  }
}
