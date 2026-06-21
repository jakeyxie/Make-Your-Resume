package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {

    List<Admin> selectAll(String name);

    List<Admin> selectAllByObject(Admin admin);

    Admin selectByEmail(String email);

    void insert(Admin admin);

    void updateById(Admin admin);

    @Delete("delete from `admin` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `admin` where id = #{id}")
    Admin selectById(String id);
}
