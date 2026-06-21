package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import com.example.entity.Validate;
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.service.ValidateService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/validate")
public class ValidateController {
    @Resource
    ValidateService validateService;

    @Resource
    UserService userService;

    @Resource
    UserMapper userMapper;
    //发送邮件的类
    @Resource
    JavaMailSender mailSender;

    //这里使用的是我们已经在配置问价中固定了的变量值,也就是通过这个邮箱向目标邮箱发送重置密码的邮件
    @Value("${spring.mail.username}")
    String from;

    /**
     * 发送忘记密码邮件请求，每日申请次数不超过20次，每次申请间隔不低于1分钟
     * @param email
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendValidationEmail")
    public Result sendValidationEmail(@RequestParam String email,
                                          HttpServletRequest request) throws MessagingException {
        User user = userService.selectByEmail(email);
        if (user == null){
            throw new CustomerException("该邮箱所属用户不存在");
        }else {
            if (validateService.sendValidateLimitation(email, 20,1)){
                // 若允许重置密码，则在validate表中插入一行数据，带有token
                Validate validate = new Validate();
                validateService.insertNewResetRecord(validate, user, UUID.randomUUID().toString());
                // 设置邮件内容
                String appUrl = request.getScheme() + "://" + request.getServerName()+":5173";
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                // multipart模式
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setFrom(from);
                mimeMessageHelper.setSubject("重置密码");
                StringBuilder sb = new StringBuilder();
                sb.append("<html><head></head>");
                sb.append("<body><h1>点击下面的链接重置密码</h1>" +
                        "<a href = "+appUrl +"/resetPassword?token="+validate.getResetToken()+">"+appUrl +"/resetPassword?token=" +validate.getResetToken()+"</a></body>");
                sb.append("</html>");
                // 启用html
                mimeMessageHelper.setText(sb.toString(), true);
                validateService.sendPasswordResetEmail(mimeMessage);

                Map<String,Object> map1=new HashMap<>();
                map1.put("token",validate.getResetToken());
                map1.put("link",appUrl +"/resetPassword?token="+validate.getResetToken());
                map1.put("message","邮件已经发送");
                return Result.success(map1);
            }else {
                throw new CustomerException("操作过于频繁，请稍后再试！");
            }
        }
    }

    /**
     * 将url的token和数据库里的token匹配，成功后便可修改密码，token有效期为5分钟
     * @param token
     * @param password
     * @param confirmPassword
     * @return
     */
    @RequestMapping(value = "/resetPassword")
    public Result resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword){
        // 通过token找到validate记录
        Validate validate= validateService.findUserByResetToken(token);
        if (validate == null){
            throw new CustomerException("该重置请求不存在");
        }else {
            if (validateService.validateLimitation(validate.getEmail(), Long.MAX_VALUE, 5, token)){
                if (password.equals(confirmPassword)) {
                    User dbUser = userMapper.selectByEmail(validate.getEmail());
                    dbUser.setPassword(password);
                    userService.update(dbUser);
                    return Result.success("成功重置密码");
                }else {
                    throw new CustomerException("确认密码和密码不一致，请重新输入");
                }
            }else {
                throw new CustomerException("该链接失效");
            }
        }
    }
}
