package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.minio.MinioProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    MinioProperties minioProperties;
    @Autowired
    MinioClient minioClient;

    // 未来会有一个全局的异常处理器统一处理异常
    @Override
    public String upload(MultipartFile file) throws Exception {

        // 未来其他代码里面如果要上传各种东西可能都需要自动 new MinioClient
        // 1、创建出一个 MinionClient
        // 2、先判断这个桶是否存在
        boolean gmall = minioClient.bucketExists(minioProperties.getBucketName());
        if (!gmall){
            // 桶不存在
            minioClient.makeBucket(minioProperties.getBucketName());
        }
        // 3、给桶里面上传文件
        // ObjectName：文件名，上传的文件名
        String name = file.getName(); // input 的 name 值
        // 得到一个唯一文件名
        String dateStr = DateUtil.formatDate(new Date());
        String fileName = UUID.randomUUID().toString().replace("-","") + "_" +
                file.getOriginalFilename();// 原始文件名
        InputStream inputStream = file.getInputStream();
        String contentType = file.getContentType();

        // 文件上传参数：long objectSize，long partSize
        PutObjectOptions options = new PutObjectOptions(file.getSize(), -1L);
        // 默认都是二进制，必须修改成对应的图片等类型
        options.setContentType(contentType);
        // 4、文件上传
        minioClient.putObject(minioProperties.getBucketName(),
                dateStr + "/" + fileName, // 自己指定的唯一名
                inputStream,
                options);
        // 5、http://192.168.200.100:9000/gmall/filename
        // 返回刚才上传文件的可访问路径
        String url = minioProperties.getEndpoint() + "/" +
                minioProperties.getBucketName() + "/" + dateStr + "/" + fileName;
        // 优化：
        // 1、文件名重名覆盖问题
        // 2、以日期作为文件夹
        return url;
    }
}
