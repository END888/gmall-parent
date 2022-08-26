package com.atguigu.gmall.product.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
// 和配置文件绑定的，自动把配置文件中 app.minio 下配置的每个属性全部都和这个 JavaBean 的属性--对应
@ConfigurationProperties(prefix = "app.minio")
@Component
public class MinioProperties {
    private String endpoint; //服务地址
    private String ak; // 用户名
    private String sk; // 密码
    private String bucketName; //桶名

    /**
     * 以后加配置，配置文件中直接添加，并在这个类中添加对应的属性
     * 以前的代码一个不改，以后的代码都能用
     * 设计模式的原则：对扩展开放、对修改关闭（开闭原则）
     */
}
