package com.example.orderingsystem.service.impl;

import com.example.orderingsystem.entity.Food;
import com.example.orderingsystem.entity.PageBean;
import com.example.orderingsystem.mapper.FoodMapper;
import com.example.orderingsystem.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 食品Service实现类
 */
@Slf4j
@Service
public class FoodServiceImpl implements FoodService {
    
    @Autowired
    private FoodMapper foodMapper;
    
    /**
     * 查询所有食品
     */
    @Override
    @Cacheable(value = "foodList", key = "'all'")
    public List<Food> selectAll() {
        log.info("从数据库查询所有食品列表");
        return foodMapper.selectAll();
    }
    
    /**
     * 分页查询
     */
    @Override
    @Cacheable(value = "foodList", key = "'page:' + #currentPage + ':' + #pageSize")
    public PageBean<Food> selectByPage(int currentPage, int pageSize) {
        log.info("从数据库查询分页食品列表: currentPage={}, pageSize={}", currentPage, pageSize);
        
        // 计算开始索引
        int begin = (currentPage - 1) * pageSize;
        
        // 查询当前页数据
        List<Food> rows = foodMapper.selectByPage(begin, pageSize);
        
        // 查询总记录数
        Long totalCount = foodMapper.selectTotalCount();
        
        // 封装PageBean对象
        return new PageBean<>(totalCount, rows);
    }
    
    /**
     * 添加食品
     */
    @Override
    @Transactional
    @CacheEvict(value = "foodList", allEntries = true)
    public void add(Food food) {
        log.info("添加食品并清除食品列表缓存: {}", food.getFdName());
        foodMapper.add(food);
    }
    
    /**
     * 根据ID删除食品
     */
    @Override
    @Transactional
    @CacheEvict(value = "foodList", allEntries = true)
    public void deleteById(int id) {
        log.info("删除食品并清除食品列表缓存: id={}", id);
        foodMapper.deleteById(id);
    }
    
    /**
     * 根据ID查询食品
     */
    @Override
    @Cacheable(value = "food", key = "#id", unless = "#result == null")
    public Food selectById(int id) {
        log.info("从数据库查询食品: id={}", id);
        return foodMapper.selectById(id);
    }
    
    /**
     * 更新食品信息
     */
    @Override
    @Transactional
    @CacheEvict(value = {"food", "foodList"}, allEntries = true)
    public void update(Food food) {
        log.info("更新食品信息并清除所有相关缓存: id={}", food.getId());
        foodMapper.update(food);
    }
    
    /**
     * 更新食品状态
     */
    @Override
    @Transactional
    @CacheEvict(value = {"food", "foodList"}, allEntries = true)
    public void updateStatus(int id, int status) {
        log.info("更新食品状态并清除所有相关缓存: id={}, status={}", id, status);
        foodMapper.updateStatus(id, status);
    }
} 