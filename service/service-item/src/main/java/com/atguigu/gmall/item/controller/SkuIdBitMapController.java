package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkuIdBitMapController {

    @GetMapping("/sync/skuid/bitmap")
    public Result syncBitMap(){
        // 1、数据库中所有商品id查询出来
        // 2、换个位置 setbit、skuids 50，0
        return Result.ok();
    }
}
