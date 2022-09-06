package com.atguigu.gmall.item;

import com.atguigu.gmall.common.annotation.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1、RedisAutoConfiguration
 *    给容器中放了 RedisTemplate<Object, Object> 和 StringRedisTemplate
 *    给redis存数据，都是k-v（v有很多类型）【string,jsonstring】
 *    StringRedisTemplate = RedisTemplate<String, String> ；
 *    给redis存数据，key是string，value序列化成字符串
 */
@EnableThreadPool
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.feign.product",
        "com.atguigu.gmall.feign.search"
})
@SpringCloudApplication
public class ItemMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class,args);
    }
}
