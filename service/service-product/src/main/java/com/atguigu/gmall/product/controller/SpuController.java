package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SPU 功能
 */
@RequestMapping("/admin/product")
@RestController
public class SpuController {

    @Autowired
    SpuInfoService spuInfoService;

    @Autowired
    SpuImageService spuImageService;

    /**
     * 分页获取 SPU
     * @Pathvariable：路径变量
     * @RequestParam：请求参数（请求体中的某个数据）
     * @RequestBody：请求参数（请求体中的所有数据拿来）
     * @RequestHeader：请求头
     * 什么是？     无论是？以后的数据还是请求体中的数据，都叫请求参数
     * http:..192.168.200.100:9000/admin/product/1/10?category3Id=2
     * ？    以前的是请求路径，@PathVariable 在这里工作
     * ？    以后请求参数：@RequestParam
     * 如果是 Post 请求，请求参数既可以放到url？以后，也可以放请求体；
     *      - @RequestParam：？以后和请求体都能取
     * 如果是 Get 请求。请求参数需要放到 url 后面
     *      - @RequestParam：》以后和请求体都能取
     *
     *  发一个请求：
     *  请求首行：GET    url
     *  请求头：Content-Type：xxx,xxx
     *  请求体：任意数据
     *  负载：请求参数（？后面的和请求体）【@RequestParam工作的地方】
     */
    @GetMapping("/{pn}/{ps}")
    public Result getSpuPage(@PathVariable("pn")Long pn,
                             @PathVariable("ps")Long ps,
                             @RequestParam("category3Id")Long category3Id){
        // long current,long size
        Page<SpuInfo> page = new Page<>(pn, ps);
        LambdaQueryWrapper<SpuInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpuInfo::getCategory3Id,category3Id);
        // 分页查询
        Page<SpuInfo> result = spuInfoService.page(page, wrapper);
        return Result.ok(result);
    }

    /**
     * SPU 定义这种商品的所有销售属性（颜色【1,2,3】、版本【4,5,6】、套餐【7,8,9】）
     * SKU 只是SPU当前定义的所有销售属性中的一个精确组合。
     *
     * 保存 SPU 信息
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo info){
        // spu_info 大保存：spu_info、spu_image、spu_sale_attr、spu_sale_attr_value
        spuInfoService.saveSpuInfo(info);
        return Result.ok();
    }

    /**
     * 查询这个 SPU 的所有图片
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result spuImageList(@PathVariable("spuId")Long spuId){
        List<SpuImage> list = spuImageService.list(new LambdaQueryWrapper<SpuImage>().eq(SpuImage::getSpuId, spuId));
        return Result.ok(list);
    }
}
