package com.example.service;

import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//注解用于标记一个类为服务层（Service Layer）组件
//该 XML 文件定义了一个查询所有管理员数据的操作，通过 MyBatis 框架将数据库结果自动映射到 User 实体类
@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public List<User> selectAll(String name){
        return userMapper.selectAll(name);
    }
    public User selectById(String id) {
        return userMapper.selectById(id);
    }
    public List<User> selectAllByObject(User user){
        return userMapper.selectAllByObject(user);
    }
    public User selectByEmail(String email){
        return userMapper.selectByEmail(email);
    }
    //登录
    public User login(Account account){
        User dbUser = userMapper.selectByEmail(account.getEmail());
        if (dbUser == null){
            throw new CustomerException("邮箱不存在");
        }
        if (!dbUser.getPassword().equals(account.getPassword())){
            throw new CustomerException("密码错误");
        }
        // 创建token并返回给前端
        String token = TokenUtils.createToken(dbUser.getId() + "-" + "user", dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }
    //注册
    public void add(User user) {
        // 根据新的账号查询数据库  是否存在同样邮箱的数据
        User dbUser = userMapper.selectByEmail(user.getEmail());
        if (dbUser != null) {
            throw new CustomerException("邮箱已被使用");
        }
        user.setRole("user");
        userMapper.insert(user);
    }
    public void register(User user) {
        this.add(user);
    }
    public void update(User user) {
        userMapper.updateById(user);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAllByObject(user);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            this.deleteById(user.getId());
        }
    }
}
