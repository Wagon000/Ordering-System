/**
 * Restaurant Ordering System - Food Entity
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

/**
 * 食品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 食品ID
     */
    private Integer id;
    
    /**
     * 食品名称
     */
    private String fdName;
    
    /**
     * 食品介绍
     */
    private String intro;
    
    /**
     * 价格
     */
    private Integer price;
    
    /**
     * 图片路径
     */
    private String photo;
    
    /**
     * 食品状态 (1:上架, 0:下架)
     */
    private Integer fdStatus;
} 