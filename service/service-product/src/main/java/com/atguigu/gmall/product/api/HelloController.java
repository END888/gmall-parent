package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 组件默认是单实例，Controller 一直在
 */
@RestController
public class HelloController {

    Map<String,String> aa = new HashMap<>();

    // 属性一直在： GC 根可达
    @GetMapping("/haha/hello")
    public Result hello(){
        String s = UUID.randomUUID().toString();
        aa.put(s,s);
        return Result.ok();
    }
}
