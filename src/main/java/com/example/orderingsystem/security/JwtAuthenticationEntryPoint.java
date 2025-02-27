/**
 * Restaurant Ordering System - JWT Authentication Entry Point
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.security;

import com.alibaba.fastjson.JSON;
import com.example.orderingsystem.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT认证入口点
 * 处理未认证的请求
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.error(401, "未认证，请先登录")));
        writer.flush();
        writer.close();
    }
} 