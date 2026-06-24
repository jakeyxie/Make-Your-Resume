package com.example.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.example.config.OssConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 服务
 */
@Service
public class OssService {

    private static final Logger log = LoggerFactory.getLogger(OssService.class);

    @Autowired
    private OssConfig ossConfig;

    /**
     * 上传文件到 OSS
     * @param file 上传的文件
     * @return 访问 URL
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        // 生成唯一文件名，避免覆盖
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = "uploads/" + System.currentTimeMillis() + "-" + UUID.randomUUID() + suffix;

        OSS ossClient = null;
        try {
            // 创建 OSS 客户端
            ossClient = new OSSClientBuilder()
                    .build(ossConfig.getEndpoint(),
                            ossConfig.getAccessKeyId(),
                            ossConfig.getAccessKeySecret());

            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(), fileName, inputStream);
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            // 构建访问 URL
            String url;
            if (ossConfig.getCdnDomain() != null && !ossConfig.getCdnDomain().isEmpty()) {
                // 使用 CDN 域名
                url = ossConfig.getCdnDomain().endsWith("/")
                        ? ossConfig.getCdnDomain() + fileName
                        : ossConfig.getCdnDomain() + "/" + fileName;
            } else {
                // 使用 OSS 外网 endpoint
                url = "https://" + ossConfig.getBucketName() + "."
                        + ossConfig.getEndpoint() + "/" + fileName;
            }

            log.info("文件上传到OSS成功: {}", url);
            return url;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除 OSS 文件
     * @param fileName 文件名（包含路径）
     */
    public void deleteFile(String fileName) {
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder()
                    .build(ossConfig.getEndpoint(),
                            ossConfig.getAccessKeyId(),
                            ossConfig.getAccessKeySecret());
            ossClient.deleteObject(ossConfig.getBucketName(), fileName);
            log.info("OSS 文件删除成功: {}", fileName);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
