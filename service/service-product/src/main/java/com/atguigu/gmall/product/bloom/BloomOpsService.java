package com.atguigu.gmall.product.bloom;

public interface BloomOpsService {

    /**
     * 重建指定布隆过滤器
     * @param bloomName
     * @param dataQueryService
     */
    void rebuildBloom(String bloomName,BloomDataQueryService dataQueryService);
}
