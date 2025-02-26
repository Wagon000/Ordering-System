/**
 * Restaurant Ordering System - JWT Authentication Filter
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.security;

import com.example.orderingsystem.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Value("${jwt.headerName}")
    private String headerName;
    
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        final String requestURI = request.getRequestURI();
        log.debug("处理请求: {}, 方法: {}", requestURI, request.getMethod());
        
        // 获取请求头中的令牌
        final String header = request.getHeader(headerName);
        
        String username = null;
        String jwtToken = null;
        
        // 检查请求头中是否有令牌
        if (header != null && header.startsWith(tokenPrefix)) {
            jwtToken = header.substring(tokenPrefix.length());
            log.debug("从请求头中获取到JWT令牌");
            
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
                log.debug("从JWT令牌中解析出用户名: {}", username);
            } catch (Exception e) {
                log.error("JWT令牌解析失败: {}", e.getMessage(), e);
            }
        } else {
            log.debug("请求头中没有JWT令牌或格式不正确");
        }
        
        // 验证令牌
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("开始验证用户: {}", username);
            
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 如果令牌有效，则设置认证信息
                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    log.info("JWT令牌验证成功，为用户 {} 设置认证信息", username);
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("认证信息已设置到SecurityContext中");
                } else {
                    log.warn("JWT令牌验证失败，用户: {}", username);
                }
            } catch (Exception e) {
                log.error("处理认证过程中发生异常: {}", e.getMessage(), e);
            }
        }
        
        chain.doFilter(request, response);
    }
} 