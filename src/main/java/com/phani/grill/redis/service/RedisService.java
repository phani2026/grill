package com.phani.grill.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RedisService {

    Map<String, List<String>> map = new ConcurrentHashMap<>();

    @Autowired
    CacheManager cacheManager;

    @Cacheable(value = "hello", key = "#key")
    public List<String> getData(String key){
        if(!map.containsKey(key)){
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(UUID.randomUUID().toString());
        return map.get(key);
    }

    public void evictData(String key){
        System.out.println("Evicting key: "+key);
        cacheManager.getCache("hello").evict(key);
        return ;
    }

}
