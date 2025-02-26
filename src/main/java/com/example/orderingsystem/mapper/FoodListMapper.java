/**
 * Restaurant Ordering System - Food List Mapper Interface
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.mapper;

import com.example.orderingsystem.entity.FoodList;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单明细Mapper接口
 */
@Repository
public interface FoodListMapper {
    
    /**
     * 批量添加订单明细
     */
    void addBatch(List<FoodList> foodLists);
    
    /**
     * 根据订单ID查询订单明细
     */
    List<FoodList> selectByOrderId(int orderId);
} 