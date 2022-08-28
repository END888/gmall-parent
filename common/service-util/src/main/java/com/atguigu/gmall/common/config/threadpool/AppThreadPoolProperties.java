package com.atguigu.gmall.common.config.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.thread-pool")
public class AppThreadPoolProperties {

    private Integer core = 2;
    private Integer max = 4;
    private Integer queueSize = 200;
    private Long keepAliveTime = 300L;
}
