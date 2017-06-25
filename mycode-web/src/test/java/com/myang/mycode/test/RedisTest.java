package com.myang.mycode.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Myang on 2017/6/25.
 */
@ContextConfiguration("classpath:spring-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        String key = "myName";
        String value = "Myang";

        set(key, value);

        String returnValue = get(key);
        logger.info("key=[{}],返回值value=[{}]", key, returnValue);
    }

    public void set(String key, String value) {
        final String vKey = key;
        final String vValue = value;
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(vKey.getBytes(), vValue.getBytes());
                return true;
            }
        });
    }

    public String get(String key) {
        final String vKey = key;
        byte[] object = (byte[]) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.get(vKey.getBytes());
            }
        });

        return new String(object);
    }
}