package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 品牌API
 */
@RestController
@RequestMapping("/admin/product")
public class BaseTrademarkController {


    @Autowired
    BaseTrademarkService baseTrademarkService;
    /**
     * 获取品牌分页列表
     * 请求参数：
     *      page：第几页
     *      limit：每页数量
     * 请求路径：
     *      http://api.gmall.com/admin/product/baseTrademark/{page}/{limit}
     * @return
     */
    @GetMapping("/baseTrademark/{pageNum}/{pageSize}")
    public Result baseTrademark(@PathVariable Long pageNum, @PathVariable Long pageSize){
        Page<BaseTrademark> page = baseTrademarkService.page(new Page<BaseTrademark>(pageNum, pageSize));
        return Result.ok(page);
    }

    /**
     * 添加品牌
     * 请求参数：
     *      {tmName: "锤子。", logoUrl: "/static/default.jpg"}
     * 请求路径：
     *      http://api.gmall.com/admin/product/baseTrademark/save
     * @return
     */
    @PostMapping("/baseTrademark/save")
    public Result save(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    /**
     * 修改品牌
     * 请求参数：
     *      baseTrademark的json字符串
     * 请求路径：
     *      http://api.gmall.com/admin/product/baseTrademark/update
     * @return
     */
    @PutMapping("/baseTrademark/update")
    public Result update(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    /**
     * 根据Id获取品牌
     * 请求参数：
     *      品牌id
     * 请求路径：
     *      http://api.gmall.com/admin/product/baseTrademark/get/{id}
     * @return
     */
    @GetMapping("/baseTrademark/get/{id}")
    public Result get(@PathVariable Long id){
        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);
    }

    /**
     * 删除品牌
     * 请求参数：
     *      品牌Id
     * 请求路径：
     *      http://api.gmall.com/admin/product/baseTrademark/remove/{id}
     * @return
     */
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result remove(@PathVariable Long id){
        baseTrademarkService.remove(new LambdaQueryWrapper<BaseTrademark>().eq(BaseTrademark::getId,id));
        return Result.ok();
    }
}
