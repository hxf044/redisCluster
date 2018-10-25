package com.redis.cluster.servie;


/**
 * Created by zhangtao on 2017/11/8.
 */

public interface RedisService {

     boolean set(String key, String value) throws RuntimeException;
     String get(String key) throws RuntimeException;
     void del(String key) throws RuntimeException;
     boolean expire(String key, long expire) throws RuntimeException;
     boolean setexpire(String key, String value, long expire) throws RuntimeException;
     boolean setObj(String key, Object obj, long expire) throws RuntimeException;


    <T> T getObj(String key, Class<T> clz) throws RuntimeException;
}
