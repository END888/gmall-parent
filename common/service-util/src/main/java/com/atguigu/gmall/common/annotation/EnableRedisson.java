package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.redisson.AppRedissonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AppRedissonAutoConfiguration.class)
public @interface EnableRedisson {
}
