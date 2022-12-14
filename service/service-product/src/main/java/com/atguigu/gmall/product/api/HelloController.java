package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.starter.cache.constant.SysRedisConst;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 组件默认是单实例，Controller 一直在
 */
@RestController
public class HelloController {

    Map<String,String> aa = new HashMap<>();
    @Autowired
    RedissonClient redissonClient;

    // 属性一直在： GC 根可达
    @GetMapping("/haha/hello")
    public Result hello(){
        String s = UUID.randomUUID().toString();
        aa.put(s,s);
        return Result.ok();
    }

    @GetMapping("/bloom/contains/{skuId}")
    public Result bloomContains(@PathVariable("skuId")Long skuId){

        // 1、拿到布隆过滤器
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);

        boolean contains = filter.contains(skuId);
        return Result.ok("布隆有吗？" + contains);
    }


}
