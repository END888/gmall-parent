package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.constant.SysRedisConst;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

//    @GetMapping({"/order/hahaha","/test/haha"})
    @GetMapping("/order/haha")
    public String orderHaha(@RequestHeader(value = SysRedisConst.USERID_HEADER,required = false,defaultValue = "没有")String uid){
        return "ok: " + uid;
    }
    @GetMapping("/order/01")
    public String order01(){
        return "ok: ";
    }
}
