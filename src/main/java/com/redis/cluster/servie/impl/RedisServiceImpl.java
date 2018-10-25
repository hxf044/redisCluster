package com.redis.cluster.servie.impl;




import com.alibaba.fastjson.JSON;
import com.redis.cluster.servie.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiaour.github.com on 2017/11/8.
 */
@Service("redisService")
@Transactional(rollbackFor = Exception.class)
public class RedisServiceImpl implements RedisService {
    private final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);
    private static int seconds= 86400;
    @Autowired
    private RedisTemplate<String, ?> redisTemplate;
    @Override
    public boolean set(final String key, final String value) throws RuntimeException {
        log.info(key,"Key is not empty.");
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }
    @Override
    public String get(final String key) throws RuntimeException {
        log.info(key,"Key is not empty.");
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value =  connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

    @Override
    public void del(final String key) throws RuntimeException {
        log.info(key,"Key is not empty.");
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection conn) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return conn.del(serializer.serialize(key));
            }
        });
    }


    @Override
    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean setexpire(String key, String value, long expire) throws RuntimeException {
        log.info(key,"Key is not empty.");
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                if(expire > 0){
                    connection.expire(serializer.serialize(key), expire);
                }
                return true;
            }
        });
        return result;
    }


    @Override
    public boolean setObj(final String key, Object obj, long expire)throws RuntimeException {
        Assert.hasText(key,"Key is not empty.");
        final String value = JSON.toJSONString(obj);
        Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                if(expire > 0){
                    connection.expire(serializer.serialize(key), expire);
                }
                return true;
            }
        });
        return result;
    }

    @Override
    public <T> T getObj(String key, Class<T> clz) throws RuntimeException {
        String value = this.get(key);
        if(value == null){
            return null;
        }
        return  JSON.parseObject(value,clz);
    }


}
