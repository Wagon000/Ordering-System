package com.example.orderingsystem.service;

import com.example.orderingsystem.entity.SystemSetting;

import java.util.List;

/**
 * 系统设置Service接口
 */
public interface SystemSettingService {
    
    /**
     * 获取所有系统设置
     */
    List<SystemSetting> getAllSettings();
    
    /**
     * 更新桌号数量
     */
    void updateTableCount(int count);
    
    /**
     * 更新配送费
     */
    void updateDeliveryFee(double fee);
} 