package com.atguigu.starter.cache.annotation;


import java.lang.annotation.*;

/**
 * 缓存注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {

    String cacheKey() default ""; // 就是cacheKey

    String bloomName() default ""; // 如果指定了布隆过滤器的名字，就用

    String bloomValue() default ""; // 指定布隆过滤器，如果需要判定的话，用声明表达式计算出的值进行判定

    String lockName() default ""; // 传入精确锁就用精确的，否则用全局默认的

    long ttl() default 60*30L;
}
