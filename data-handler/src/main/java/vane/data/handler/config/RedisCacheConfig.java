package vane.data.handler.config;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConfigurationProperties(prefix = "spring.cache.redis")
@Data
public class RedisCacheConfig {

  private Duration timeToLive = Duration.ZERO;

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory connFactory) {
    RedisSerializer<String> redisSerializer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

    // 解决查询缓存转化异常的问题
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    objectMapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance,
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY);
    jsonRedisSerializer.setObjectMapper(objectMapper);

    // 配置序列化（解决乱码问题）
    RedisCacheConfiguration config =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(timeToLive)
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer))
            .disableCachingNullValues();
    return RedisCacheManager.builder(connFactory).cacheDefaults(config).build();
  }
}
