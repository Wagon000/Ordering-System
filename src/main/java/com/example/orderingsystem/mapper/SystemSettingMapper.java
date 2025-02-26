/**
 * Restaurant Ordering System - System Setting Mapper Interface
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.mapper;

import com.example.orderingsystem.entity.SystemSetting;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统设置Mapper接口
 */
@Repository
public interface SystemSettingMapper {
    
    /**
     * 查询所有系统设置
     */
    List<SystemSetting> selectAll();
    
    /**
     * 根据键查询设置
     */
    SystemSetting selectByKey(String key);
    
    /**
     * 更新设置值
     */
    void updateValue(String key, String value);
} 