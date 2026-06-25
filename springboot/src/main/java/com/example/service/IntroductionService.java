package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.Account;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.mapper.CategoryMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IntroductionService {

    @Resource
    IntroductionMapper introductionMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    UserMapper userMapper;

    @Transactional
    public void add(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introduction.setUserId(currentUser.getId());
        introduction.setTime(DateUtil.now());
        introductionMapper.insert(introduction);
    }

    @Transactional
    public void update(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introductionMapper.updateById(introduction);
    }

    @Transactional
    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        introductionMapper.deleteById(id);
    }

    public List<Introduction> selectAll(Introduction introduction) {
        List<Introduction> introductions = introductionMapper.selectAll(introduction);
        for (Introduction dbIntroduction : introductions) {
            dbIntroduction.setDescription(HtmlUtil.cleanHtmlTag(dbIntroduction.getContent()));
        }
        return introductions;
    }

    public PageInfo<Introduction> selectPage(Integer pageNum, Integer pageSize, Introduction introduction) {
        PageHelper.startPage(pageNum, pageSize);
        List<Introduction> list = introductionMapper.selectAll(introduction);
        for (Introduction dbIntroduction : list) {
            dbIntroduction.setDescription(HtmlUtil.cleanHtmlTag(dbIntroduction.getContent()));
        }
        return PageInfo.of(list);
    }

    public Introduction selectById(Integer id) {
        Introduction dbIntroduction = introductionMapper.selectById(id);
        Integer categoryId = dbIntroduction.getCategoryId();
        Integer userId = dbIntroduction.getUserId();
        Category category = categoryMapper.selectById(categoryId);
        User user = userMapper.selectById(userId.toString());
        if (ObjectUtil.isNotEmpty(category)) {
            dbIntroduction.setCategoryTitle(category.getTitle());
        }
        if (ObjectUtil.isNotEmpty(user)) {
            dbIntroduction.setUserName(user.getUsername());
            dbIntroduction.setUserAvatar(user.getAvatar());
        }
        return dbIntroduction;
    }
}