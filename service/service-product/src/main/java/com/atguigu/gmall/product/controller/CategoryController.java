package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  分类请求处理器
 *  前后端分离：前端发送请求，后台处理好后响应 JSON 数据
 *  所有请求全部返回 Result 对象的 JSON，所有要携带的数据放到 Result 的 data 属性内即可
 */
// @ResponseBody    所有的响应数据都直接写给浏览器（如果是对象写成 json，如果是文本就写成普通字符串）
// @Controller 这个类是来接收请求的
@RequestMapping("/admin/product") // 抽取公共路径
@RestController
public class CategoryController {

    @Autowired
    BaseCategory1Service baseCategory1Service;

    @Autowired
    BaseCategory2Service baseCategory2Service;

    @Autowired
    BaseCategory3Service baseCategory3Service;


    /**
     * 获取一级分类
     * http://api.gmall.com/admin/product/getCategory1
     * @return
     */
    @GetMapping("/getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> category1List = baseCategory1Service.list();
        return Result.ok(category1List);
    }

    /**
     * 获取二级分类
     * http://api.gmall.com/admin/product/getCategory2/{category1Id}
     * @param c1id
     * @return
     */
    @GetMapping("/getCategory2/{c1id}")
    public Result getCategory2(@PathVariable Long c1id){
        List<BaseCategory2> category2List = baseCategory2Service.getCategory1Child(c1id);
        return Result.ok(category2List);
    }

    /**
     * 获取三级分类
     * http://api.gmall.com/admin/product/getCategory3/{category2Id}
     * @param c2id
     * @return
     */
    @GetMapping("/getCategory3/{c2id}")
    public Result getCategory3(@PathVariable Long c2id){
        List<BaseCategory3> category2List = baseCategory3Service.getCategory2Child(c2id);
        return Result.ok(category2List);
    }
}
