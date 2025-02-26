/**
 * Restaurant Ordering System - Food Mapper Interface
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.mapper;

import com.example.orderingsystem.entity.Food;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 食品Mapper接口
 */
@Repository
public interface FoodMapper {
    
    /**
     * 查询所有食品
     */
    List<Food> selectAll();
    
    /**
     * 查询总记录数
     */
    Long selectTotalCount();
    
    /**
     * 分页查询
     */
    List<Food> selectByPage(@Param("begin") int begin, @Param("size") int size);
    
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
    void updateStatus(@Param("id") int id, @Param("status") int status);
} 