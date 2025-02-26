package com.example.orderingsystem.service;

import com.example.orderingsystem.entity.FoodList;
import com.example.orderingsystem.entity.Order;
import com.example.orderingsystem.entity.PageBean;

import java.util.List;

/**
 * 订单Service接口
 */
public interface OrderService {
    
    /**
     * 添加订单
     */
    Order add(Order order, List<FoodList> foodLists);
    
    /**
     * 分页查询
     */
    PageBean<Order> selectByPage(int currentPage, int pageSize);
    
    /**
     * 根据用户ID查询订单
     */
    List<Order> selectByUserId(int userId);
    
    /**
     * 更新订单状态
     */
    void updateStatus(int id, int status);
    
    /**
     * 更新订单评分
     */
    void updateStar(int id, int star);
    
    /**
     * 根据订单ID查询订单明细
     */
    List<FoodList> selectFoodListByOrderId(int orderId);
    
    /**
     * 获取已占用的桌号列表
     */
    List<Integer> getOccupiedTables();
    
    /**
     * 更新订单桌号
     */
    void updateTableNumber(int id, int tableNumber);
} 