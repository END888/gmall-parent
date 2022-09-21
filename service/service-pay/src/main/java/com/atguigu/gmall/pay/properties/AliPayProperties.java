package com.atguigu.gmall.pay.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.pay")
@Data
public class AliPayProperties {

    // 网关地址
    private String gatewayUrl;
    // 应用ID
    private String appId;
    // 应用私钥
    private String merchantPrivateKey;
    // 字符集
    private String charset;
    // 请求格式，固定为 json
    private String format;
    // 支付宝公钥证书路径
    private String alipayPublicKey;
    // 签名类型
    private String signType;
    // 同步返回地址
    private String returnUrl;
    // 异步回调地址
    private String notifyUrl;
}
