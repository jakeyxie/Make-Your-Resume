package com.example.service;

import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    @Resource
    AdminMapper adminMapper;
    @Resource
    PasswordEncoder passwordEncoder;

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
    @Transactional
    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByEmail(admin.getEmail());
        if (dbAdmin != null) {
            throw new CustomException("邮箱已被使用");
        }
        admin.setRole("admin");
        // 密码加密
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminMapper.insert(admin);
    }

    public void register(Admin admin) {
        this.add(admin);
    }

    //登录
    public Admin login(Account account){
        Admin dbAdmin = adminMapper.selectByEmail(account.getEmail());
        if (dbAdmin == null){
            throw new CustomException("邮箱不存在");
        }
        if (!passwordEncoder.matches(account.getPassword(), dbAdmin.getPassword())){
            throw new CustomException("密码错误");
        }
        // 创建token并返回给前端
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + "admin", dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    @Transactional
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAllByObject(admin);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    @Transactional
    public void deleteBatch(List<Admin> list) {
        for (Admin admin : list) {
            if (admin.getId() != null) {
                this.deleteById(admin.getId());
            }
        }
    }
}
