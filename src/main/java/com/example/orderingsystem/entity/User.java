/**
 * Restaurant Ordering System - User Entity
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
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Integer id;
    
    /**
     * 用户名
     */
    private String usName;
    
    /**
     * 密码
     */
    private String psd;
    
    /**
     * 角色 (-1:管理员, 0:普通用户)
     */
    private Integer role;
    
    /**
     * 电话号码
     */
    private String phone;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 账户余额
     */
    private Double balance;
    
    /**
     * 总消费金额
     */
    private Double totalConsumption;
} 