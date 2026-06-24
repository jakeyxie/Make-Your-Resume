package com.example.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    @Resource
    private String jwtSecret;

    static String staticJwtSecret;

    @PostConstruct
    public void init() {
        staticJwtSecret = jwtSecret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StrUtil.isBlank(token)) {
            throw new CustomException("401", "您无权限操作");
        }
        Account account = null;
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String userId = split[0];
            String role = split[1];
            if ("admin".equals(role)) {
                account = adminService.selectById(userId);
            } else if ("user".equals(role)) {
                account = userService.selectById(userId);
            }
        } catch (Exception e) {
            throw new CustomException("401", "您无权限操作");
        }
        if (account == null) {
            throw new CustomException("401", "您无权限操作");
        }
        try {
            // 使用统一的JWT密钥验证签名
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(staticJwtSecret)).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new CustomException("401", "您无权限操作");
        }
        return true;
    }
}
