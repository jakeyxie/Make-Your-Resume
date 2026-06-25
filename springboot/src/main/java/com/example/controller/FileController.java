package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.service.OssService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理文件上传下载相关的接口（使用阿里云 OSS）
 */
@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private OssService ossService;
    @Autowired
    private UserService userService;

    /**
     * 文件上传到 OSS
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new CustomException("上传文件不能为空");
        }
        String url = ossService.uploadFile(file);
        User user = new User();
        user.setAvatar( url);
        userService.update(user);
        return Result.success(url);
    }

    /**
     * 文件下载（OSS 文件通过 URL 直接访问，此处保留兼容接口）
     */
    @GetMapping("/download/{fileName}")
    public Result download(@PathVariable String fileName) throws Exception {
        // OSS 文件通过 URL 直接访问，返回 URL 即可
        // 保留此接口用于兼容旧逻辑
        return Result.success(fileName);
    }

    /**
     * wang-editor编辑器文件上传接口
     */
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        try {
            String url = ossService.uploadFile(file);
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("errno", 0);
            resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", url)));
            return resMap;
        } catch (Exception e) {
            log.error("wang-editor 文件上传失败", e);
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("errno", 1);
            resMap.put("message", "文件上传失败");
            return resMap;
        }
    }
}
