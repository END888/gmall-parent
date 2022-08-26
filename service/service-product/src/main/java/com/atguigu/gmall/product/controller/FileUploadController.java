package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * 文件上传
 */
@Api("文件上传控制器")
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    /**
     * 文件上传功能
     * 1、前端把文件流放到哪里了？我们该怎么拿到？
     *      Post请求数据在请求体（包含了文件[流]）
     * 如何接收：
     * @RequestParam("file") MultipartFile file
     * @RequestPart("file") NumtipartFile file: 专门处理文件的
     *
     * 各种注解接不通位置的请求数据
     * @RequestParam: 无论是什么请求，接请求参数：用一个 Pojo 把所有数据都接收了
     * @RequestPart: 接收请求参数里面的文件项
     * @RequestBody: 接收请求体中的所有数据（json转为 pojo）
     * @PathVariable: 按路径上的动态变量
     * @RequestHeader: 获取浏览器发送的请求的请求头中的某些值
     * @CookieValue: 获取浏览器发送的请求中的 Cookie 值
     * - 如果多个就写数组，否则就写单个对象
     * @return
     */
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file")MultipartFile file) throws Exception {
        String url = fileUploadService.upload(file);
        // TODO 文件上传，怎么上传到 Minio?
        return Result.ok(url);
    }

    @PostMapping("/register")
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("header") MultipartFile[] header,
                           @RequestParam("sfz") MultipartFile sfz,
                           @RequestParam("shz") MultipartFile shz){
        HashMap<String, Object> map = new HashMap<>();
        map.put("用户名",username);
        map.put("密码",password);
        map.put("邮箱",email);
        map.put("头像文件个数",header.length);
        map.put("身份证文件大小",sfz.getSize());
        map.put("生活照文件大小",shz.getSize());
        return Result.ok(map);
    }
}
