package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.CategoryFeignClient;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class IndexController {


    @Autowired
    CategoryFeignClient categoryFeignClient;

    @GetMapping({"/","/index"})
    public String indexPage(Model model){

        log.info("{}","访问首页啦-----------");
        // 远程查询出所有菜单，封装成一个树形结构的模型
        Result<List<CategoryTreeTo>> result = categoryFeignClient.getAllCategoryWithTree();
        log.info("查询到的数据：{}",result);
        if (result.isOk()){
            // 远程成功
            List<CategoryTreeTo> data = result.getData();
            model.addAttribute("list",data);
        }
        // classpath:/templates/index/index.html
        return "index/index";// 页面的逻辑视图名
    }
}
