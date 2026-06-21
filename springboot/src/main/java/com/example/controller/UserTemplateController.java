package com.example.controller;

import com.example.common.Result;
import com.example.entity.UserTemplate;
import com.example.service.UserTemplateService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userTemplate")
public class UserTemplateController {

    @Resource
    UserTemplateService userTemplateService;

    @PostMapping("/add")
    public Result add(@RequestBody UserTemplate userTemplate) {  // @RequestBody 接收前端传来的 json参数
        userTemplateService.add(userTemplate);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody UserTemplate userTemplate) {  // @RequestBody 接收前端传来的 json参数
        userTemplateService.update(userTemplate);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        userTemplateService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/userTemplate/selectAll
    public Result selectAll(UserTemplate userTemplate) {
        List<UserTemplate> userTemplateList = userTemplateService.selectAll(userTemplate);
        return Result.success(userTemplateList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             UserTemplate userTemplate) {
        PageInfo<UserTemplate> pageInfo = userTemplateService.selectPage(pageNum, pageSize, userTemplate);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

    /**
     * 根据id查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        UserTemplate userTemplate = userTemplateService.selectById(id);
        return Result.success(userTemplate);
    }

}
