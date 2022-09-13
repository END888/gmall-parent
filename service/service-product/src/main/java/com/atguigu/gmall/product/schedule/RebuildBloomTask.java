package com.atguigu.gmall.product.schedule;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.starter.cache.constant.SysRedisConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 重建布隆任务
 */
@Service
public class RebuildBloomTask {

    @Autowired
    BloomOpsService bloomOpsService;

    @Autowired
    BloomDataQueryService bloomDataQueryService;

    /**
     * 每隔 7 天重建一次：bitmap（更符合 sku 的场景）
     * corn 表达式：* * * * * ? *
     * 秒 分 时 日 月 周 年
     */
    @Scheduled(cron = "0 0 3 ? * 3")
    public void rebuild(){
        System.out.println("重建测试。。。。");
        bloomOpsService.rebuildBloom(SysRedisConst.BLOOM_SKUID,bloomDataQueryService);
    }

}
