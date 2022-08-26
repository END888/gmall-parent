package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SKU 管理
 */
@RequestMapping("/admin/product")
@RestController
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;

    /**
     * sku 分页查询
     * @param pn
     * @param ps
     * @return
     */
    @GetMapping("/list/{pn}/{ps}")
    public Result getSkuList(@PathVariable("pn")Long pn,
                             @PathVariable("ps")Long ps){
        Page<SkuInfo> page = new Page<>(pn,ps);
        Page<SkuInfo> result = skuInfoService.page(page);
        return Result.ok(result);
    }

    /**
     * 接收前端的 json 数据，可以使用逆向方式生成 vo【和前端对接的 JavaBean】
     * https://www.json.cn/json/json2java.html  根据 JSON 模型生成 vo
     * @param info
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSku(@RequestBody SkuInfo info){
        // sku 的大保存
        skuInfoService.saveSkuInfo(info);
        return Result.ok();
    }

    /**
     * 商品下架
     * @param skuId
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId")Long skuId){
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }

    /**
     * 商品上架
     * @param skuId
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId")Long skuId){
        skuInfoService.onSale(skuId);
        return Result.ok();
    }
}
