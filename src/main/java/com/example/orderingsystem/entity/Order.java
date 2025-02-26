/**
 * Restaurant Ordering System - Order Entity
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单ID
     */
    private Integer id;
    
    /**
     * 下单时间
     */
    private Date time;
    
    /**
     * 用户ID
     */
    private Integer usId;
    
    /**
     * 订单总金额
     */
    private Integer sum;
    
    /**
     * 评分 (-1:未评分)
     */
    private Integer star;
    
    /**
     * 订单状态 (-1:未完成, 1:已完成)
     */
    private Integer orStatus;
    
    /**
     * 订单类型 (1:堂食, 2:外卖)
     */
    private Integer orderType;
    
    /**
     * 桌号
     */
    private Integer tableNumber;
    
    /**
     * 联系电话
     */
    private String phone;
    
    /**
     * 送餐地址
     */
    private String address;
    
    /**
     * 备注
     */
    private String remark;
} 