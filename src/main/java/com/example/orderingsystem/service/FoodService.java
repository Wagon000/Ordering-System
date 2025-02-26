package com.example.orderingsystem.service;

import com.example.orderingsystem.entity.Food;
import com.example.orderingsystem.entity.PageBean;

import java.util.List;

/**
 * 食品Service接口
 */
public interface FoodService {
    
    /**
     * 查询所有食品
     */
    List<Food> selectAll();
    
    /**
     * 分页查询
     */
    PageBean<Food> selectByPage(int currentPage, int pageSize);
    
    /**
     * 添加食品
     */
    void add(Food food);
    
    /**
     * 根据ID删除食品
     */
    void deleteById(int id);
    
    /**
     * 根据ID查询食品
     */
    Food selectById(int id);
    
    /**
     * 更新食品信息
     */
    void update(Food food);
    
    /**
     * 更新食品状态
     */
    void updateStatus(int id, int status);
} 