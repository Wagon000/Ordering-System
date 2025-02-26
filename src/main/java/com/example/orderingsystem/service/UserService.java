package com.example.orderingsystem.service;

import com.example.orderingsystem.entity.PageBean;
import com.example.orderingsystem.entity.User;

/**
 * 用户Service接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    User login(String username, String password);
    
    /**
     * 用户注册
     */
    boolean register(User user);
    
    /**
     * 分页查询
     */
    PageBean<User> selectByPage(int currentPage, int pageSize);
    
    /**
     * 根据ID删除用户
     */
    void deleteById(int id);
    
    /**
     * 根据ID查询用户
     */
    User selectById(int id);
    
    /**
     * 更新用户信息
     */
    void update(User user);
    
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(String username);
    
    /**
     * 修改密码
     */
    boolean changePassword(String username, String oldPassword, String newPassword);
} 