package com.example.service;

import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//注解用于标记一个类为服务层（Service Layer）组件
//该 XML 文件定义了一个查询所有管理员数据的操作，通过 MyBatis 框架将数据库结果自动映射到 Admin 实体类
@Service
public class AdminService {
    @Resource
    AdminMapper adminMapper;

    public List<Admin> selectAll(String name){
        return adminMapper.selectAll(name);
    }

    public Admin selectById(String id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAllByObject(Admin admin){
        return adminMapper.selectAllByObject(admin);
    }

    public Admin selectByEmail(String email){
        return adminMapper.selectByEmail(email);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(name);
        return PageInfo.of(list);
    }
    //注册账号
    public void add(Admin admin) {
        // 根据新的账号查询数据库  是否存在同样邮箱的数据
        Admin dbAdmin = adminMapper.selectByEmail(admin.getEmail());
        if (dbAdmin != null) {
            throw new CustomerException("邮箱已被使用");
        }
        admin.setRole("admin");
        adminMapper.insert(admin);
    }
    public void register(Admin admin) {
        this.add(admin);
    }
    //登录
    public Admin login(Account account){
        Admin dbAdmin = adminMapper.selectByEmail(account.getEmail());
        if (dbAdmin == null){
            throw new CustomerException("邮箱不存在");
        }
        if (!dbAdmin.getPassword().equals(account.getPassword())){
            throw new CustomerException("密码错误");
        }
        // 创建token并返回给前端
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + "admin", dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAllByObject(admin);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Admin> list) {
        for (Admin admin : list) {
            this.deleteById(admin.getId());
        }
    }
}
