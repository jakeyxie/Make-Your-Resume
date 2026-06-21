package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.Template;
import com.example.service.TemplateService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/template")
public class TemplateController {
    @Resource
    TemplateService templateService;

    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Template> templateList = templateService.selectAll(null,null);
        return Result.success(templateList);
    }
    /*分页查询*/
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String style){
        PageInfo<Template> pageInfo = templateService.selectPage(pageNum, pageSize,name,style);
        return Result.success(pageInfo);
    }
}
