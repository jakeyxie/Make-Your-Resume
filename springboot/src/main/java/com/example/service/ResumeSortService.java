package com.example.service;

import com.example.entity.ResumeSort;
import com.example.entity.ResumeSort;
import com.example.mapper.ResumeSortMapper;
import com.example.mapper.ResumeSortMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeSortService {
    @Resource
    ResumeSortMapper resumeSortMapper;

    public List<ResumeSort> selectAllSorts(){
        return resumeSortMapper.selectAllSorts();
    }
    
    public void add(ResumeSort resumeSort) {
        resumeSortMapper.insert(resumeSort);
    }

    public void update(ResumeSort resumeSort) {
        resumeSortMapper.updateById(resumeSort);
    }

    public void deleteById(Integer id) {
        resumeSortMapper.deleteById(id);
    }

    public List<ResumeSort> selectAll(ResumeSort resumeSort) {
        return resumeSortMapper.selectAll(resumeSort);
    }

    public PageInfo<ResumeSort> selectPage(Integer pageNum, Integer pageSize, ResumeSort resumeSort) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<ResumeSort> list = resumeSortMapper.selectAll(resumeSort);
        return PageInfo.of(list);
    }
}
