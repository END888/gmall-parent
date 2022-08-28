package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 可配置的线程池，可自动注入
     */
    @Autowired
    ThreadPoolExecutor executor;

    /**
     *  异步实际上是： 空间换时间；  new Thread()
     *  最一行： 串行： 6s
     *  最一行： 并行： 等待一个最长时间，全部任务都能完成。
     *  如果异步：  new Thread().start();
     *  不能直接用 new Thread().start();
     *         一个请求进来，直接无脑开6个线程，高并发下直接OOM。
     *         一个一炸可能导致整个集群雪崩。
     *         不能无脑开线程，很容易资源耗尽，池技术（线程池、连接池、xxx池）【资源复用问题】
     *  线程池+阻塞队列：解决资源复用与等待问题。
     *
     *  6个任务都结束后，To才能返回
     *         1、CompletableFuture 异步【编排】
     *     启动一个异步任务有多少种方法
     *         1、new Thread().start()
     *         2、Runnable  new Thread(runnable).start();
     *         3、Callable  带结果  FutureTask
     *         4、线程池
     *              executor.submit(()->{});  executor.execute(()->{});
     *         5、异步编排 CompletableFuture
     *             - CompletableFuture启动异步任务
     */
    public SkuDetailTo getSkuDetailFromRpc(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();

        // 1、查基本信息 1s
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            detailTo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);

        // 2、查商品图片信息 1s
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
        }, executor);

        // 3、查商品实时价格 2s
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> price = skuDetailFeignClient.getSku1010Price(skuId);
            detailTo.setPrice(price.getData());
        }, executor);

        // 4、查询销售属性名值
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Long spuId = skuInfo.getSpuId();
            Result<List<SpuSaleAttr>> saleAttrValues = skuDetailFeignClient.getSkuSaleattrvalues(skuId, spuId);
            detailTo.setSpuSaleAttrList(saleAttrValues.getData());
        }, executor);

        // 5、查询sku组合
        CompletableFuture<Void> skuValueFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<String> sKuValueJson = skuDetailFeignClient.getSKuValueJson(skuInfo.getSpuId());
            detailTo.setValuesSkuJson(sKuValueJson.getData());
        }, executor);

        // 6、查分类
        CompletableFuture<Void> categoryFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
            detailTo.setCategoryView(categoryView.getData());
        }, executor);

        // 等所有任务都执行结束
        CompletableFuture
                .allOf(imageFuture,priceFuture,saleAttrFuture,skuValueFuture,categoryFuture)
                .join();

        return detailTo;
    }

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        // 1、看缓存中有没有 sku:info:50
        String jsonStr = redisTemplate.opsForValue().get("sku:info" + skuId);
        if ("x".equals(jsonStr)){
            // 说明以前查过，只不过数据库没有此记录，为了避免再次回源，缓存一个空占位符
            return null;
        }

        // 说明此次没有查过
        if (StringUtils.isEmpty(jsonStr)){
            // 2、redis没有缓存数据
            // 2.1、回源；之前可以判断 redis 中保存的 sku 的 id 集合，有没有这个id
            // 防止随机值穿透攻击
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            // 2.2、放入缓存【查到的对象转为json字符串保存到 redis】
            String cacheJson = "x";
            if (fromRpc!=null){
                cacheJson = Jsons.toStr(fromRpc);
                redisTemplate.opsForValue().set("sku:info:" + skuId,cacheJson,7, TimeUnit.DAYS);
            }else {
                redisTemplate.opsForValue().set("sku:info:" + skuId,cacheJson,30,TimeUnit.MINUTES);
            }
            return fromRpc;
        }
        // 3、缓存中有，把json转成指定的对象
        SkuDetailTo skuDetailTo = Jsons.toObj(jsonStr, SkuDetailTo.class);
        return skuDetailTo;
    }
}
