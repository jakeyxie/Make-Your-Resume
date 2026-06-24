package com.example.service;

import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    PasswordEncoder passwordEncoder;

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
            throw new CustomException("邮箱不存在");
        }
        if (!passwordEncoder.matches(account.getPassword(), dbUser.getPassword())){
            throw new CustomException("密码错误");
        }
        // 创建token并返回给前端
        String token = TokenUtils.createToken(dbUser.getId() + "-" + "user", dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    //注册
    @Transactional
    public void add(User user) {
        User dbUser = userMapper.selectByEmail(user.getEmail());
        if (dbUser != null) {
            throw new CustomException("邮箱已被使用");
        }
        user.setRole("user");
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

    public void register(User user) {
        this.add(user);
    }

    @Transactional
    public void update(User user) {
        userMapper.updateById(user);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAllByObject(user);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    @Transactional
    public void deleteBatch(List<User> list) {
        for (User user : list) {
            if (user.getId() != null) {
                this.deleteById(user.getId());
            }
        }
    }
}
