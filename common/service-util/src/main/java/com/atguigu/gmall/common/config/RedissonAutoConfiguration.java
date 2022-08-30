package com.atguigu.gmall.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1、导入 redisson.jar
 * 2、给容器中放好 RedissonClient 组件
 *
 * 如果 redis 没有配置好，redisson能不能用
 */
@AutoConfigureAfter(RedisAutoConfiguration.class) // 表示我当前组件在该组件之后配置
@Configuration
public class RedissonAutoConfiguration {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedissonClient redisClient(){
        // 1、创建一个配置
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        // 2、指定好 redisson 的配置项 【注意：Redis url should start with redis:// or rediss:// (for SSL connection)】
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);

        // 3、创建一个 RedissonClient
        RedissonClient client = Redisson.create(config);
        return client;
    }
}
