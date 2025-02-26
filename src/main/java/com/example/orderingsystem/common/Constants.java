/**
 * Restaurant Ordering System - System Constants
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.common;

/**
 * 系统常量类
 */
public class Constants {
    
    /**
     * 用户角色：管理员
     */
    public static final int ROLE_ADMIN = -1;
    
    /**
     * 用户角色：普通用户
     */
    public static final int ROLE_USER = 0;
    
    /**
     * 订单状态：未完成
     */
    public static final int ORDER_STATUS_UNFINISHED = -1;
    
    /**
     * 订单状态：已完成
     */
    public static final int ORDER_STATUS_FINISHED = 1;
    
    /**
     * 食品状态：下架
     */
    public static final int FOOD_STATUS_OFF = 0;
    
    /**
     * 食品状态：上架
     */
    public static final int FOOD_STATUS_ON = 1;
    
    /**
     * 订单评分：未评分
     */
    public static final int ORDER_STAR_UNRATED = -1;
    
    /**
     * Redis缓存键前缀：用户
     */
    public static final String CACHE_KEY_USER = "user:";
    
    /**
     * Redis缓存键前缀：食品
     */
    public static final String CACHE_KEY_FOOD = "food:";
    
    /**
     * Redis缓存键前缀：订单
     */
    public static final String CACHE_KEY_ORDER = "order:";
    
    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
} 