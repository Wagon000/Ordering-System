package com.example.orderingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统设置实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSetting implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 设置ID
     */
    private Integer id;
    
    /**
     * 设置键
     */
    private String settingKey;
    
    /**
     * 设置值
     */
    private String settingValue;
    
    /**
     * 设置描述
     */
    private String description;
} 