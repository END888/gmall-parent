package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class ThreadController {

    @Autowired
    ThreadPoolExecutor executor;
    @GetMapping("/thread/get")
    public Result getThread(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("线程池类型",executor.getClass().getName());
        map.put("线程数",executor.getActiveCount());
        map.put("核心线程数",executor.getCorePoolSize());
        AtomicReference<String> threadName = new AtomicReference<>("");
        CompletableFuture.runAsync(()->{
            threadName.set(Thread.currentThread().getName());
        },executor).join();
        map.put("线程名",threadName);
        return Result.ok(map);
    }
}
