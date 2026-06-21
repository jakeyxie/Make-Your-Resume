package com.example.mapper;

import com.example.entity.ResumeSort;
import com.example.entity.ResumeSort;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ResumeSortMapper {
    List<ResumeSort> selectAllSorts();
    
    List<ResumeSort> selectAll(ResumeSort resumeSort);

    void insert(ResumeSort resumeSort);

    void updateById(ResumeSort resumeSort);

    @Delete("delete from `resumesort` where resumeSortId = #{resumeSortId}")
    void deleteById(Integer resumeSortId);

    @Select("select * from resumesort where resumeSortId = #{resumeSortId}")
    ResumeSort selectById(Integer resumeSortId);
}
