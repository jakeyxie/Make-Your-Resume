package com.example.mapper;

import com.example.entity.Template;

import java.util.List;

public interface TemplateMapper {
    List<Template> selectAll(String name,String style);
}
