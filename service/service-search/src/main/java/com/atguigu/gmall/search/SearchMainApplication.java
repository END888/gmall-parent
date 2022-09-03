package com.atguigu.gmall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories // 开启 ES 的自动仓库功能,写 Bean、写接口，会自动创建好索引库bin设置好 Mapping 映射
@SpringCloudApplication
public class SearchMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchMainApplication.class,args);
    }
}
