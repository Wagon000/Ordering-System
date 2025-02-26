/**
 * Restaurant Ordering System - Order Mapper Interface
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.mapper;

import com.example.orderingsystem.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单Mapper接口
 */
@Repository
public interface OrderMapper {
    
    /**
     * 添加订单
     */
    void add(Order order);
    
    /**
     * 查询总记录数
     */
    Long selectTotalCount();
    
    /**
     * 分页查询
     */
    List<Order> selectByPage(@Param("begin") int begin, @Param("size") int size);
    
    /**
     * 根据用户ID查询订单
     */
    List<Order> selectByUserId(int userId);
    
    /**
     * 更新订单状态
     */
    void updateStatus(@Param("id") int id, @Param("status") int status);
    
    /**
     * 更新订单评分
     */
    void updateStar(@Param("id") int id, @Param("star") int star);

    /**
     * 获取所有未完成订单的桌号
     */
    List<Integer> selectOccupiedTables();

    /**
     * 更新订单桌号
     */
    void updateTableNumber(@Param("id") int id, @Param("tableNumber") int tableNumber);
} 