/**
 * Restaurant Ordering System - Admin Controller
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.controller;

import com.example.orderingsystem.common.Result;
import com.example.orderingsystem.entity.User;
import com.example.orderingsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 初始化管理员账户
     * 注意：此接口仅用于开发和测试环境，生产环境应该禁用
     */
    @GetMapping("/init")
    public Result<User> initAdmin() {
        log.info("初始化管理员账户");
        
        try {
            // 检查是否已存在管理员账户
            User existingAdmin = userService.selectByUsername("admin");
            if (existingAdmin != null) {
                log.info("管理员账户已存在，ID: {}", existingAdmin.getId());
                
                // 更新管理员账户
                existingAdmin.setPsd(passwordEncoder.encode("admin123"));
                existingAdmin.setRole(-1);
                existingAdmin.setPhone("13800000000");
                existingAdmin.setAddress("系统管理员");
                existingAdmin.setBalance(0.0);
                existingAdmin.setTotalConsumption(0.0);
                
                userService.update(existingAdmin);
                log.info("管理员账户已更新");
                
                return Result.success("管理员账户已更新", existingAdmin);
            }
            
            // 创建新的管理员账户
            User admin = new User();
            admin.setUsName("admin");
            admin.setPsd(passwordEncoder.encode("admin123"));
            admin.setRole(-1);
            admin.setPhone("13800000000");
            admin.setAddress("系统管理员");
            admin.setBalance(0.0);
            admin.setTotalConsumption(0.0);
            
            boolean result = userService.register(admin);
            if (result) {
                log.info("管理员账户创建成功");
                return Result.success("管理员账户创建成功", admin);
            } else {
                log.warn("管理员账户创建失败");
                return Result.error("管理员账户创建失败");
            }
        } catch (Exception e) {
            log.error("初始化管理员账户异常: {}", e.getMessage(), e);
            return Result.error("初始化管理员账户异常: " + e.getMessage());
        }
    }
} 