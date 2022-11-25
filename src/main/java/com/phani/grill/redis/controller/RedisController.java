package com.phani.grill.redis.controller;

import com.phani.grill.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/redis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedisController {

    @Autowired
    RedisService redisService;



    @GetMapping("/cache")
    List<String> get(@RequestParam String key){
        return redisService.getData(key);
    }


    @GetMapping("/evict")
    String evictData(@RequestParam String key){
        redisService.evictData(key);
        return key;
    }







}
