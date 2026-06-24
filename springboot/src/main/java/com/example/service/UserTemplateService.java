package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTemplateService {

    @Resource
    UserTemplateMapper userTemplateMapper;
    @Resource
    ResumeSortMapper resumeSortMapper;
    @Resource
    UserMapper userMapper;

    @Transactional
    public void add(UserTemplate userTemplate) {
        Account currentUser = TokenUtils.getCurrentUser();
        userTemplate.setUserId(currentUser.getId());
        userTemplate.setTime(DateUtil.now());
        userTemplateMapper.insert(userTemplate);
    }

    @Transactional
    public void update(UserTemplate userTemplate) {
        userTemplateMapper.updateById(userTemplate);
    }

    @Transactional
    public void deleteById(Integer id) {
        userTemplateMapper.deleteById(id);
    }

    public List<UserTemplate> selectAll(UserTemplate userTemplate) {
        return userTemplateMapper.selectAll(userTemplate);
    }

    public PageInfo<UserTemplate> selectPage(Integer pageNum, Integer pageSize, UserTemplate userTemplate) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("user".equals(currentUser.getRole())) {
            userTemplate.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<UserTemplate> list = userTemplateMapper.selectAll(userTemplate);
        return PageInfo.of(list);
    }

    public UserTemplate selectById(Integer id) {
        UserTemplate dbUserTemplate = userTemplateMapper.selectById(id);
        Integer resumeId = dbUserTemplate.getResumeId();
        Integer userId = dbUserTemplate.getUserId();
        ResumeSort resumeSort = resumeSortMapper.selectById(resumeId);
        User user = userMapper.selectById(userId.toString());
        if (ObjectUtil.isNotEmpty(resumeSort)) {
            dbUserTemplate.setResumeSort(resumeSort.getSort());
        }
        if (ObjectUtil.isNotEmpty(user)) {
            dbUserTemplate.setUsername(user.getUsername());
            dbUserTemplate.setUserAvatar(user.getAvatar());
        }
        return dbUserTemplate;
    }
}
