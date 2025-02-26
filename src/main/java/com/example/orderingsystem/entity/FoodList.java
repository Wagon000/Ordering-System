package com.example.orderingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单明细实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodList implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 食品ID
     */
    private Integer fdId;
    
    /**
     * 订单ID
     */
    private Integer orId;
    
    /**
     * 食品名称
     */
    private String fdName;
    
    /**
     * 单价
     */
    private Integer price;
    
    /**
     * 数量
     */
    private Integer count;
    
    /**
     * 小计
     */
    private Integer sum;
} 