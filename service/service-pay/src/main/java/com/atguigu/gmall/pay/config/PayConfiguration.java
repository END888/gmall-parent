package com.atguigu.gmall.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.atguigu.gmall.pay.properties.AliPayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfiguration {

    @Autowired
    AliPayProperties aliPayProperties;

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                aliPayProperties.getGatewayUrl(),
                aliPayProperties.getAppId(),
                aliPayProperties.getMerchantPrivateKey(),
                aliPayProperties.getFormat(),
                aliPayProperties.getCharset(),
                aliPayProperties.getAlipayPublicKey(),
                aliPayProperties.getSignType());
    }
}
