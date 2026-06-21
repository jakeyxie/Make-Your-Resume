package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    List<User> selectAll(String name);
    List<User> selectAllByObject(User user);
    User selectByEmail(String email);
    void insert(User user);
    void updateById(User user);

    @Delete("delete from `user` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `user` where id = #{id}")
    User selectById(String id);
}
