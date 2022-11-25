package com.phani.grill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean(destroyMethod="shutdown")
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useSingleServer().setAddress("redis://"+redisHostName+":"+redisPort);
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        config.put("grill", new CacheConfig(30*60*1000, 12*60*1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
