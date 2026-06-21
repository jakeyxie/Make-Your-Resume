package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.Account;
import com.example.entity.ResumeSort;
import com.example.entity.UserTemplate;
import com.example.entity.User;
import com.example.mapper.ResumeSortMapper;
import com.example.mapper.UserTemplateMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTemplateService {

    @Resource
    UserTemplateMapper userTemplateMapper;
    @Resource
    ResumeSortMapper resumeSortMapper;
    @Resource
    UserMapper userMapper;

    public void add(UserTemplate userTemplate) {
        Account currentUser = TokenUtils.getCurrentUser();
        userTemplate.setUserId(currentUser.getId());
        userTemplate.setTime(DateUtil.now());
        userTemplateMapper.insert(userTemplate);
    }

    public void update(UserTemplate userTemplate) {
        Account currentUser = TokenUtils.getCurrentUser();
        userTemplateMapper.updateById(userTemplate);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        userTemplateMapper.deleteById(id);
    }

    public List<UserTemplate> selectAll(UserTemplate userTemplate) {
        List<UserTemplate> userTemplates = userTemplateMapper.selectAll(userTemplate);
        return userTemplates;
    }

    public PageInfo<UserTemplate> selectPage(Integer pageNum, Integer pageSize, UserTemplate userTemplate) {
        // 查之前要先给他条件
        Account currentUser = TokenUtils.getCurrentUser();
        if ("user".equals(currentUser.getRole())) {
            userTemplate.setUserId(currentUser.getId());
        }
        else if ("admin".equals(currentUser.getRole())) {
            userTemplate.setUserId(currentUser.getId());
        }
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<UserTemplate> list = userTemplateMapper.selectAll(userTemplate);
        return PageInfo.of(list);
    }

    public UserTemplate selectById(Integer id) {
        UserTemplate dbUserTemplate = userTemplateMapper.selectById(id);
        // 先拿到resumeId
        Integer resumeId = dbUserTemplate.getResumeId();
        Integer userId = dbUserTemplate.getUserId();
        // 通过resumeId从resumeSort表里通过主键查询出分类数据
        ResumeSort resumeSort = resumeSortMapper.selectById(resumeId);
        User user = userMapper.selectById(userId.toString());
        if (ObjectUtil.isNotEmpty(resumeSort)) {
            // 把分类的title赋值给resumeSortTitle
            dbUserTemplate.setResumeSort(resumeSort.getSort());
        }
        if (ObjectUtil.isNotEmpty(user)) {
            dbUserTemplate.setUsername(user.getUsername());
            dbUserTemplate.setUserAvatar(user.getAvatar());
        }
        return dbUserTemplate;
    }
}
