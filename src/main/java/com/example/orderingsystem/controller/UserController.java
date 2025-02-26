/**
 * Restaurant Ordering System - User Controller
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.controller;

import com.example.orderingsystem.common.Result;
import com.example.orderingsystem.entity.PageBean;
import com.example.orderingsystem.entity.User;
import com.example.orderingsystem.service.UserService;
import com.example.orderingsystem.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户Controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        log.info("接收到登录请求: 用户名={}", user.getUsName());
        
        try {
            User loginUser = userService.login(user.getUsName(), user.getPsd());
            if (loginUser != null) {
                log.info("用户 {} 登录成功, 角色={}", loginUser.getUsName(), loginUser.getRole());
                
                // 生成JWT令牌
                String token = jwtUtil.generateToken(loginUser);
                log.debug("为用户 {} 生成JWT令牌", loginUser.getUsName());
                
                // 返回用户信息和令牌
                Map<String, Object> data = new HashMap<>();
                data.put("user", loginUser);
                data.put("token", token);
                
                return Result.success("登录成功", data);
            }
            
            log.warn("用户 {} 登录失败: 用户名或密码错误", user.getUsName());
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            log.error("用户 {} 登录过程中发生异常: {}", user.getUsName(), e.getMessage(), e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        log.info("用户注册开始: {}", user);
        
        try {
            boolean result = userService.register(user);
            if (result) {
                log.info("用户注册成功: {}", user.getUsName());
                return Result.success("注册成功", null);
            } else {
                log.warn("用户注册失败，用户名已存在: {}", user.getUsName());
                return Result.error("用户名已存在");
            }
        } catch (Exception e) {
            log.error("用户注册异常: {}", e.getMessage(), e);
            return Result.error("系统异常，请联系管理员");
        }
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageBean<User>> selectByPage(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("分页查询用户: currentPage={}, pageSize={}", currentPage, pageSize);
        
        PageBean<User> pageBean = userService.selectByPage(currentPage, pageSize);
        return Result.success(pageBean);
    }
    
    /**
     * 根据ID删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteById(@PathVariable int id) {
        log.info("删除用户: id={}", id);
        
        userService.deleteById(id);
        return Result.success();
    }
    
    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<User> selectById(@PathVariable int id) {
        log.info("查询用户: id={}", id);
        
        User user = userService.selectById(id);
        if (user != null) {
            return Result.success(user);
        }
        
        return Result.error("用户不存在");
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        log.info("更新用户: id={}", user.getId());
        
        userService.update(user);
        return Result.success();
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getCurrentUserInfo() {
        // 从SecurityContext中获取当前用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("获取当前用户信息: {}", username);
        
        User user = userService.selectByUsername(username);
        if (user != null) {
            return Result.success(user);
        }
        
        return Result.error("用户不存在");
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody Map<String, String> params) {
        // 从SecurityContext中获取当前用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("修改密码: {}", username);
        
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        if (oldPassword == null || newPassword == null) {
            return Result.error("密码不能为空");
        }
        
        try {
            boolean result = userService.changePassword(username, oldPassword, newPassword);
            if (result) {
                return Result.success("密码修改成功", null);
            } else {
                return Result.error("原密码错误");
            }
        } catch (Exception e) {
            log.error("修改密码异常: {}", e.getMessage(), e);
            return Result.error("系统异常，请联系管理员");
        }
    }
} 