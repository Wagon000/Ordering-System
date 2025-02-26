package com.example.orderingsystem.service.impl;

import com.example.orderingsystem.entity.SystemSetting;
import com.example.orderingsystem.mapper.SystemSettingMapper;
import com.example.orderingsystem.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统设置Service实现类
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService {
    
    @Autowired
    private SystemSettingMapper systemSettingMapper;
    
    @Override
    public List<SystemSetting> getAllSettings() {
        return systemSettingMapper.selectAll();
    }
    
    @Override
    @Transactional
    public void updateTableCount(int count) {
        systemSettingMapper.updateValue("table_count", String.valueOf(count));
    }
    
    @Override
    @Transactional
    public void updateDeliveryFee(double fee) {
        systemSettingMapper.updateValue("delivery_fee", String.valueOf(fee));
    }
} 