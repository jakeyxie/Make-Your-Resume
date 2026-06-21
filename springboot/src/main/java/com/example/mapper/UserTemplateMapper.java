package com.example.mapper;

import com.example.entity.UserTemplate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserTemplateMapper {

    List<UserTemplate> selectAll(UserTemplate userTemplate);

    void insert(UserTemplate userTemplate);

    void updateById(UserTemplate userTemplate);

    @Delete("delete from `userTemplate` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from userTemplate where id = #{id}")
    UserTemplate selectById(Integer id);
}
