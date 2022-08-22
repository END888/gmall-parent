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

@RequestMapping("/admin/product")
@RestController
public class CategoryController {

    @Autowired
    BaseCategory1Service baseCategory1Service;

    @Autowired
    BaseCategory2Service baseCategory2Service;

    @Autowired
    BaseCategory3Service baseCategory3Service;

    @GetMapping("/getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> category1List = baseCategory1Service.list();
        return Result.ok(category1List);
    }

    @GetMapping("/getCategory2/{c1id}")
    public Result getCategory2(@PathVariable String c1id){
        List<BaseCategory2> category2List = baseCategory2Service.getCategoryChild(c1id);
        return Result.ok(category2List);
    }

    @GetMapping("/getCategory3/{c2id}")
    public Result getCategory3(@PathVariable String c2id){
        List<BaseCategory3> category2List = baseCategory3Service.getCategoryChild(c2id);
        return Result.ok(category2List);
    }
}
