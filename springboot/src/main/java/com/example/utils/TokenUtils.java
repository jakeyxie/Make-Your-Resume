package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * JWT Token工具类
 */
@Component
public class TokenUtils {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    @Resource
    private String jwtSecret;

    static AdminService staticAdminService;
    static UserService staticUserService;
    static String staticJwtSecret;

    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticUserService = userService;
        staticJwtSecret = jwtSecret;
    }

    /**
     * 生成token
     */
    public static String createToken(String data, String sign) {
        return JWT.create()
                .withAudience(data)
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1))
                .sign(Algorithm.HMAC256(staticJwtSecret));
    }

    /**
     * 获取当前登录的用户信息
     */
    public static Account getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                return null;
            }
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String userId = split[0];
            String role = split[1];
            if ("admin".equals(role)) {
                return staticAdminService.selectById(userId);
            } else if ("user".equals(role)) {
                return staticUserService.selectById(userId);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
