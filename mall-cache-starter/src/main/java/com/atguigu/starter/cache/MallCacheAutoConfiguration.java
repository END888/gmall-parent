package com.atguigu.starter.cache;

import com.atguigu.starter.cache.aspect.CacheAspect;
import com.atguigu.starter.cache.servie.CacheOpsService;
import com.atguigu.starter.cache.servie.impl.CacheOpsServiceImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 以前容器中的所有组件要导入进去
 * 整个缓存场景涉及到的所有组件都得注入到容器中
 */
//@Import(CacheAspect.class)
@EnableAspectJAutoProxy
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class MallCacheAutoConfiguration {
    @Autowired
    RedisProperties redisProperties;

    /**
     * 缓存切面
     * @return
     */
    @Bean
    public CacheAspect cacheAspect(){
        return new CacheAspect();
    }

    /**
     * 缓存操作类
     * @return
     */
    @Bean
    public CacheOpsService cacheOpsService(){
        return new CacheOpsServiceImpl();
    }

    @Bean
    public RedissonClient redissonClient(){
        // 1、创建一个配置
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        // 2、指定好 redisson 的配置项
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
        // 3、创建一个 RedissonClient
        RedissonClient client = Redisson.create(config);
        //Redis url should start with redis:// or rediss:// (for SSL connection)
        return client;
    }
}
