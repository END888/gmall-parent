package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    /**
     * 文件上传
     * @param file  需要上传的文件
     * @return  返回文件在 minio 中的存储地址
     * @throws Exception
     */
    String upload(MultipartFile file) throws Exception;
}
