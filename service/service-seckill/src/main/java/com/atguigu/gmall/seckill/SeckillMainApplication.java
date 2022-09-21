package com.atguigu.gmall.seckill;


import com.atguigu.gmall.annotation.EnableAppRabbit;
import com.atguigu.gmall.common.annotation.EnableAutoExceptionHandler;
import com.atguigu.gmall.common.annotation.EnableAutoFeignInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAutoFeignInterceptor
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.feign.user",
        "com.atguigu.gmall.feign.order"
}) //只会扫描主程序所在的子包
@EnableAppRabbit
@EnableAutoExceptionHandler
@EnableScheduling
@MapperScan("com.atguigu.gmall.seckill.mapper")
@SpringCloudApplication
public class SeckillMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillMainApplication.class,args);
    }
}
