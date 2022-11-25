package com.phani.grill.controller;

import com.phani.grill.service.GrillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrillController {

    @Autowired
    GrillService grillService;



    @GetMapping("/cache")
    List<String> get(@RequestParam String key){
        return grillService.getData(key);
    }


    @GetMapping("/evict")
    String evictData(@RequestParam String key){
        grillService.evictData(key);
        return key;
    }







}
