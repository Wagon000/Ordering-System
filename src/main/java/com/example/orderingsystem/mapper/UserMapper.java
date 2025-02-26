/**
 * Restaurant Ordering System - User Mapper Interface
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.mapper;

import com.example.orderingsystem.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Repository
public interface UserMapper {
    
    /**
     * 根据用户名和密码查询用户
     */
    User login(@Param("username") String username, @Param("password") String password);
    
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(String username);
    
    /**
     * 添加用户
     */
    void add(User user);
    
    /**
     * 查询总记录数
     */
    Long selectTotalCount();
    
    /**
     * 分页查询
     */
    List<User> selectByPage(@Param("begin") int begin, @Param("size") int size);
    
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
} 