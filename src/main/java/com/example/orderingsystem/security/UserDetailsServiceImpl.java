/**
 * Restaurant Ordering System - User Details Service Implementation
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.security;

import com.example.orderingsystem.entity.User;
import com.example.orderingsystem.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情服务
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Spring Security正在加载用户: {}", username);
        
        try {
            // 查询用户
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                log.warn("用户不存在: {}", username);
                throw new UsernameNotFoundException("用户不存在");
            }
            
            log.info("找到用户: {}, 角色: {}", username, user.getRole());
            
            // 设置权限
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (user.getRole() != null && user.getRole() == -1) {
                log.info("为用户 {} 添加管理员权限", username);
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                log.info("为用户 {} 添加普通用户权限", username);
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            
            // 返回UserDetails
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsName(),
                    user.getPsd(),
                    authorities
            );
            
            log.info("成功加载用户详情: {}, 权限: {}", username, authorities);
            return userDetails;
        } catch (Exception e) {
            log.error("加载用户详情时发生异常: {}", e.getMessage(), e);
            throw e;
        }
    }
} 