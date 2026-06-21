package com.example.controller;

import com.example.common.Result;
import com.example.entity.ResumeSort;
import com.example.service.ResumeSortService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumesort")
public class ResumeSortController {
    @Resource
    ResumeSortService resumeSortService;

    @PostMapping("/add")
    public Result add(@RequestBody ResumeSort resumeSort) {  // @RequestBody 接收前端传来的 json参数
        resumeSortService.add(resumeSort);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody ResumeSort resumeSort) {  // @RequestBody 接收前端传来的 json参数
        resumeSortService.update(resumeSort);
        return Result.success();
    }

    @DeleteMapping("/delete/{resumeSortId}")
    public Result delete(@PathVariable Integer resumeSortId) {  // @PathVariable 接收前端传来的路径参数
        resumeSortService.deleteById(resumeSortId);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/resumeSort/selectAll
    public Result selectAll(ResumeSort resumeSort) {
        List<ResumeSort> resumeSortList = resumeSortService.selectAll(resumeSort);
        return Result.success(resumeSortList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             ResumeSort resumeSort) {
        PageInfo<ResumeSort> pageInfo = resumeSortService.selectPage(pageNum, pageSize, resumeSort);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

    @GetMapping("/selectAllSorts")
    public Result selectAllSorts(){
        List<ResumeSort> resumesortList = resumeSortService.selectAllSorts();
        return Result.success(resumesortList);
    }
}
