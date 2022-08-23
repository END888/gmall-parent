package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台属性管理
 */
@RestController
@RequestMapping("/admin/product/")
public class BaseAttrController {


    @Autowired
    BaseAttrInfoService baseAttrInfoService;
    /**
     * 根据分类id获取平台属性
     * 请求路径：
     *      http://api.gmall.com/admin/product/attrInfoList/{category1Id}/{category2Id}/{category3Id}
     * @param category1Id 一级分类ID
     * @param category2Id 二级分类ID
     * @param category3Id 三级分类ID
     * @return
     */
    @GetMapping("attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable Long category1Id, @PathVariable Long category2Id, @PathVariable Long category3Id){
        List<BaseAttrInfo> attrInfoList = baseAttrInfoService.getBaseAttrInfoByCategoryId(category1Id,category2Id,category3Id);
        return Result.ok(attrInfoList);
    }

    /**
     * 保存、修改属性信息二合一的方法
     * 前端把所有页面录入的数据以json的方式post传给我们
     *
     * 取出前端发送的请求的请求体中的数据 @RequestBody，
     * 并把这个数据（json）转为指定的 BaseAtrrInfo 对象，
     * BaseAttrInfo 封装前端提交来的所有数据
     *
     * 请求体：
     *      {"id":null,"attrName":"上市日期","category1Id":0,"category2Id":0,"category3Id":0,"attrValueList":[{"valueName":"2019","edit":false},{"valueName":"2020","edit":false},{"valueName":"2021","edit":false},{"valueName":"2022","edit":false}],"categoryId":2,"categoryLevel":1}
     * 请求路径：
     *      http://192.168.200.1/admin/product/saveAttrInfo
     * @return
     */
    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        baseAttrInfoService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    /**
     * 根据平台属性ID获取平台属性对象数据
     * attrId：平台属性ID
     * 请求路径：
     *      http://api.gmall.com/admin/product/getAttrValueList/{attrId}
     * @return
     */
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable Long attrId){
        List<BaseAttrValue> values = baseAttrInfoService.getBaseAttrValueByAttrId(attrId);
        return Result.ok(values);
    }



}
