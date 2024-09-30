package com.example.tastyhub.common.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@EnableCaching
public class RedisConfig {

  @Value("${spring.data.redis1.host}")
  private String verifiedHost;

  @Value("${spring.data.redis1.port}")
  private int verifiedPort;

  @Value("${spring.data.redis2.host}")
  private String cacheHost;

  @Value("${spring.data.redis2.port}")
  private int cachePort;

//    @Value("${spring.data.redis.password}")
//    private String password;

  @Bean
  public RedisConnectionFactory redisConnectionFactoryForVerified() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(verifiedHost);
    redisStandaloneConfiguration.setPort(verifiedPort);
//        redisStandaloneConfiguration.setPassword(password);
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
        redisStandaloneConfiguration);
    return lettuceConnectionFactory;
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplateForVerified() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactoryForVerified());
    redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키 직렬화
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 값 직렬화
    return redisTemplate;
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactoryForCache() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(cacheHost);
    redisStandaloneConfiguration.setPort(cachePort);
//        redisStandaloneConfiguration.setPassword(password);
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
        redisStandaloneConfiguration);
    return lettuceConnectionFactory;
  }

  @Bean(name ="redisTemplate")
  public RedisTemplate<?, ?> redisTemplateForCache() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactoryForCache());
    redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키 직렬화
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 값 직렬화
    return redisTemplate;
  }

  @Bean
  public CacheManager contentCacheManager(@Qualifier("redisConnectionFactoryForCache") RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
            new GenericJackson2JsonRedisSerializer())) // Value Serializer 변경
        .entryTtl(Duration.ofMinutes(3L)); // 캐시 수명 30분

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
        .cacheDefaults(redisCacheConfiguration).build();
  }

  // 검증 및 토큰용 StringRedisTemplate
  @Bean
  public StringRedisTemplate verifiedRedisTemplate() {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(redisConnectionFactoryForVerified());  // 포트 6379에 연결됨
    return template;
  }

  // 캐시용 StringRedisTemplate
  @Bean
  public StringRedisTemplate cacheRedisTemplate() {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(redisConnectionFactoryForCache());  // 포트 6380에 연결됨
    return template;
  }


}
