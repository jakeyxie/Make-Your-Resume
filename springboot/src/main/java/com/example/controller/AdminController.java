package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    @GetMapping("/selectAll")
    public Result selectAll(String name){
        List<Admin> adminList = adminService.selectAll(null);
        return Result.success(adminList);
    }
    @GetMapping("/selectAllByObject")  //   完整的请求路径：http://ip:port/admin/selectAll
    public Result selectAllByObject(Admin admin) {
        List<Admin> adminList = adminService.selectAllByObject(admin);
        return Result.success(adminList);
    }
    @PostMapping("/forgetPassword")
    public Result login(@RequestBody String email) {
        Admin dbAdmin = adminService.selectByEmail(email);
        if (dbAdmin != null) {
            return Result.success(dbAdmin.getEmail());
        }
        return Result.error("邮箱错误");
    }

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        Admin dbAdmin = adminService.login(admin);
        return Result.success(dbAdmin);
    }
    @PostMapping("/register")
    public Result register(@RequestBody Admin admin) {
        adminService.register(admin);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        adminService.deleteById(id);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {  // @RequestBody 接收前端传来的 json参数
        adminService.update(admin);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Admin admin) {
        PageInfo<Admin> pageInfo = adminService.selectPage(pageNum, pageSize, admin);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> list) {  //  @RequestBody 接收前端传来的 json数组
        adminService.deleteBatch(list);
        return Result.success();
    }

    /**
     * 数据导出
     * ids: 1,2,3
     */
    @GetMapping("/export")
    public void exportData(Admin admin, HttpServletResponse response) throws Exception {
        String ids = admin.getIds();
        if (StrUtil.isNotBlank(ids)) {
            String[] idsArr = ids.split(",");
            admin.setIdsArr(idsArr);
        }
        // 1. 拿到所有数据
        List<Admin> list = adminService.selectAllByObject(admin);
        // 2. 构建Writer对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("surname", "签名");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(list);
        // 5. 设置输出的文件的名称以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6. 写出到输出流 并关闭 writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }

    /**
     * 批量导入
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        //  1. 拿到输入流 构建 reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //  2. 通过Reader 读取 excel 里面的数据
        reader.addHeaderAlias("用户名", "username");
        reader.addHeaderAlias("签名", "surname");
        reader.addHeaderAlias("电话", "phone");
        reader.addHeaderAlias("邮箱", "email");
        List<Admin> list = reader.readAll(Admin.class);
        // 3. 将数据写到数据库
        for (Admin admin : list) {
            adminService.add(admin);
        }
        return Result.success();
    }
}
