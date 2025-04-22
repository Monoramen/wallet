package com.mono.wallet.impl.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mono.wallet.api.dto.WalletResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfig {
    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);


        try {
            jedisConnectionFactory.afterPropertiesSet();
            jedisConnectionFactory.getConnection().ping();
            log.info("Success to connect to Redis: {}:{}", host, port);
        } catch (Exception e) {
            log.error("Error connect Redis: {}. Ran without ", e.getMessage());
        }

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, WalletResponseDTO> redisTemplate() {
        RedisTemplate<String, WalletResponseDTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        Jackson2JsonRedisSerializer<WalletResponseDTO> serializer = new Jackson2JsonRedisSerializer<>(WalletResponseDTO.class);
        serializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }



    @Bean
    public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {
        Jackson2JsonRedisSerializer<WalletResponseDTO> serializer =
                new Jackson2JsonRedisSerializer<>(WalletResponseDTO.class);

        serializer.setObjectMapper(objectMapper);

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );

        return RedisCacheManager.builder(jedisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }


}