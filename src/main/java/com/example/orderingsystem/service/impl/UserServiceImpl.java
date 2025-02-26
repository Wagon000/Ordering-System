package com.example.orderingsystem.service.impl;

import com.example.orderingsystem.entity.PageBean;
import com.example.orderingsystem.entity.User;
import com.example.orderingsystem.mapper.UserMapper;
import com.example.orderingsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户Service实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 用户登录
     */
    @Override
    public User login(String username, String password) {
        log.info("尝试登录用户: {}", username);
        
        // 查询用户
        User user = userMapper.selectByUsername(username);
        
        // 检查用户是否存在
        if (user == null) {
            log.warn("登录失败: 用户 {} 不存在", username);
            return null;
        }
        
        log.info("找到用户: {}, 角色: {}", username, user.getRole());
        log.debug("数据库中的密码: {}", user.getPsd());
        log.debug("用户输入的密码: {}", password);
        
        // 验证密码
        boolean passwordMatches = passwordEncoder.matches(password, user.getPsd());
        log.info("密码验证结果: {}", passwordMatches);
        
        if (passwordMatches) {
            log.info("密码验证成功，用户 {} 登录成功", username);
            return user;
        } else {
            log.warn("登录失败: 用户 {} 密码不匹配", username);
            // 尝试直接比较（用于调试）
            boolean plainMatch = password.equals(user.getPsd());
            log.debug("明文密码比较结果: {}", plainMatch);
            return null;
        }
    }
    
    /**
     * 用户注册
     */
    @Override
    @Transactional
    public boolean register(User user) {
        log.info("开始注册用户: {}", user);
        
        try {
            // 检查用户名是否已存在
            User existUser = userMapper.selectByUsername(user.getUsName());
            if (existUser != null) {
                log.warn("用户名已存在: {}", user.getUsName());
                return false;
            }
            
            // 密码加密
            String rawPassword = user.getPsd();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            log.debug("密码加密: {} -> {}", rawPassword, encodedPassword);
            user.setPsd(encodedPassword);
            
            // 如果角色为null，则设置为普通用户
            if (user.getRole() == null) {
                log.debug("角色为null，设置为普通用户");
                user.setRole(0);
            } else {
                log.debug("使用指定角色: {}", user.getRole());
            }
            
            // 如果余额为null，则设置默认余额
            if (user.getBalance() == null) {
                log.debug("余额为null，设置默认余额: 99999元");
                user.setBalance(99999.00);
            }
            
            // 如果总消费金额为null，则设置为0
            if (user.getTotalConsumption() == null) {
                log.debug("总消费金额为null，设置为0元");
                user.setTotalConsumption(0.00);
            }
            
            log.info("添加用户到数据库: {}", user);
            userMapper.add(user);
            log.info("用户注册成功，ID: {}", user.getId());
            return true;
        } catch (Exception e) {
            log.error("用户注册异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 分页查询
     */
    @Override
    public PageBean<User> selectByPage(int currentPage, int pageSize) {
        // 计算开始索引
        int begin = (currentPage - 1) * pageSize;
        
        // 查询当前页数据
        List<User> rows = userMapper.selectByPage(begin, pageSize);
        
        // 查询总记录数
        Long totalCount = userMapper.selectTotalCount();
        
        // 封装PageBean对象
        return new PageBean<>(totalCount, rows);
    }
    
    /**
     * 根据ID删除用户
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        userMapper.deleteById(id);
    }
    
    /**
     * 根据ID查询用户
     */
    @Override
    public User selectById(int id) {
        return userMapper.selectById(id);
    }
    
    /**
     * 更新用户信息
     */
    @Override
    @Transactional
    public void update(User user) {
        // 如果密码不为空，则进行加密
        if (user.getPsd() != null && !user.getPsd().isEmpty()) {
            user.setPsd(passwordEncoder.encode(user.getPsd()));
        } else {
            // 如果密码为空，则不更新密码
            User oldUser = userMapper.selectById(user.getId());
            user.setPsd(oldUser.getPsd());
        }
        
        userMapper.update(user);
    }
    
    /**
     * 根据用户名查询用户
     */
    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    /**
     * 修改密码
     */
    @Override
    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // 查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return false;
        }
        
        // 验证原密码
        if (!passwordEncoder.matches(oldPassword, user.getPsd())) {
            return false;
        }
        
        // 更新密码
        user.setPsd(passwordEncoder.encode(newPassword));
        userMapper.update(user);
        
        return true;
    }
} 