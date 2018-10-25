package com.redis.cluster.web;

import com.redis.cluster.servie.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/10/25.
 */
@RestController
@RequestMapping("/api/redis")
public class RedisController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

    @GetMapping("/set/{key}/{value}")
    public String set(
            @PathVariable("key")  String key ,
            @PathVariable("value") String value){
        redisService.set(key,value);
        return "ok";
    }

    @GetMapping("/get/{key}")
    public String get(
            @PathVariable("key")  String key ){
        return  redisService.get(key);
    }
}
