/**
 * Restaurant Ordering System - System Setting Controller
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.controller;

import com.example.orderingsystem.common.Result;
import com.example.orderingsystem.entity.SystemSetting;
import com.example.orderingsystem.service.SystemSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统设置Controller
 */
@Slf4j
@RestController
@RequestMapping("/settings")
public class SystemSettingController {
    
    @Autowired
    private SystemSettingService systemSettingService;
    
    /**
     * 获取所有系统设置
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<SystemSetting>> getAllSettings() {
        log.info("获取所有系统设置");
        List<SystemSetting> settings = systemSettingService.getAllSettings();
        return Result.success(settings);
    }
    
    /**
     * 更新桌号数量
     */
    @PutMapping("/table-count")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateTableCount(@RequestBody Map<String, Integer> params) {
        log.info("更新桌号数量: {}", params);
        Integer count = params.get("count");
        if (count == null || count < 1) {
            return Result.error("桌号数量必须大于0");
        }
        systemSettingService.updateTableCount(count);
        return Result.success();
    }
    
    /**
     * 更新配送费
     */
    @PutMapping("/delivery-fee")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateDeliveryFee(@RequestBody Map<String, Double> params) {
        log.info("更新配送费: {}", params);
        Double fee = params.get("fee");
        if (fee == null || fee < 0) {
            return Result.error("配送费不能小于0");
        }
        systemSettingService.updateDeliveryFee(fee);
        return Result.success();
    }
} 