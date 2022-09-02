package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.item.SkuDetailFeignClient;
import com.atguigu.gmall.model.to.SkuDetailTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品详情
 */
@Slf4j
@Controller
public class ItemController {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    /**
     * 商品详情页
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId")Long skuId, Model model){
        log.info("商品详细信息查询-------------------------------------------------------------------------------------");
        // 远程查询出商品的详细信息
        Result<SkuDetailTo> result = skuDetailFeignClient.getSkuDetail(skuId);
        log.info("查询结果：{}",result);

        if (result.isOk()){
            SkuDetailTo skuDetailTo = result.getData();

            model.addAttribute("categoryView",skuDetailTo.getCategoryView());
            model.addAttribute("skuInfo",skuDetailTo.getSkuInfo());
            model.addAttribute("price",skuDetailTo.getPrice());
            model.addAttribute("spuSaleAttrList",skuDetailTo.getSpuSaleAttrList()); // spu 的销售属性列表
            model.addAttribute("valuesSkuJson",skuDetailTo.getValuesSkuJson()); // json
        }
        return "item/index";
    }

}
