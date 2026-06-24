package com.example.service;

import com.example.entity.Template;
import com.example.mapper.TemplateMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    @Resource
    TemplateMapper templateMapper;

    public List<Template> selectAll(String name, String style) {
        return templateMapper.selectAll(name, style);
    }

    public PageInfo<Template> selectPage(Integer pageNum, Integer pageSize, String name, String style) {
        PageHelper.startPage(pageNum, pageSize);
        List<Template> list = templateMapper.selectAll(name, style);
        return PageInfo.of(list);
    }
}
