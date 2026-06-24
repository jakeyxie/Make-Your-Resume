package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {
    /**
     * OSS 端点
     */
    private String endpoint;
    /**
     * 访问密钥 ID
     */
    private String accessKeyId;
    /**
     * 访问密钥 Secret
     */
    private String accessKeySecret;
    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * CDN 域名（可选，用于返回访问 URL）
     */
    private String cdnDomain;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getCdnDomain() {
        return cdnDomain;
    }

    public void setCdnDomain(String cdnDomain) {
        this.cdnDomain = cdnDomain;
    }
}