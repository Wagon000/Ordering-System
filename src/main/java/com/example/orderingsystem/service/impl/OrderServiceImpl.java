package com.example.orderingsystem.service.impl;

import com.example.orderingsystem.entity.FoodList;
import com.example.orderingsystem.entity.Order;
import com.example.orderingsystem.entity.PageBean;
import com.example.orderingsystem.entity.User;
import com.example.orderingsystem.mapper.FoodListMapper;
import com.example.orderingsystem.mapper.OrderMapper;
import com.example.orderingsystem.mapper.UserMapper;
import com.example.orderingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private FoodListMapper foodListMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 添加订单
     */
    @Override
    @Transactional
    public Order add(Order order, List<FoodList> foodLists) {
        // 设置默认值
        if (order.getStar() == null) {
            order.setStar(-1);
        }
        if (order.getOrStatus() == null) {
            order.setOrStatus(-1);
        }
        
        // 处理用户余额和消费金额
        User user = userMapper.selectById(order.getUsId());
        if (user != null) {
            // 检查余额是否足够
            double orderAmount = order.getSum() / 100.0; // 将分转换为元
            if (user.getBalance() < orderAmount) {
                throw new RuntimeException("余额不足，无法下单");
            }
            
            // 扣除余额
            user.setBalance(user.getBalance() - orderAmount);
            
            // 增加消费金额
            if (user.getTotalConsumption() == null) {
                user.setTotalConsumption(orderAmount);
            } else {
                user.setTotalConsumption(user.getTotalConsumption() + orderAmount);
            }
            
            // 更新用户信息
            userMapper.update(user);
        }
        
        // 添加订单
        orderMapper.add(order);
        
        // 设置订单ID
        for (FoodList foodList : foodLists) {
            foodList.setOrId(order.getId());
        }
        
        // 批量添加订单明细
        foodListMapper.addBatch(foodLists);
        
        return order;
    }
    
    /**
     * 分页查询
     */
    @Override
    public PageBean<Order> selectByPage(int currentPage, int pageSize) {
        // 计算开始索引
        int begin = (currentPage - 1) * pageSize;
        
        // 查询当前页数据
        List<Order> rows = orderMapper.selectByPage(begin, pageSize);
        
        // 查询总记录数
        Long totalCount = orderMapper.selectTotalCount();
        
        // 封装PageBean对象
        return new PageBean<>(totalCount, rows);
    }
    
    /**
     * 根据用户ID查询订单
     */
    @Override
    public List<Order> selectByUserId(int userId) {
        return orderMapper.selectByUserId(userId);
    }
    
    /**
     * 更新订单状态
     */
    @Override
    @Transactional
    public void updateStatus(int id, int status) {
        orderMapper.updateStatus(id, status);
    }
    
    /**
     * 更新订单评分
     */
    @Override
    @Transactional
    public void updateStar(int id, int star) {
        orderMapper.updateStar(id, star);
    }
    
    /**
     * 根据订单ID查询订单明细
     */
    @Override
    public List<FoodList> selectFoodListByOrderId(int orderId) {
        return foodListMapper.selectByOrderId(orderId);
    }
    
    /**
     * 获取已占用的桌号列表
     */
    @Override
    public List<Integer> getOccupiedTables() {
        return orderMapper.selectOccupiedTables();
    }
    
    /**
     * 更新订单桌号
     */
    @Override
    @Transactional
    public void updateTableNumber(int id, int tableNumber) {
        // 检查桌号是否已被占用
        List<Integer> occupiedTables = orderMapper.selectOccupiedTables();
        if (occupiedTables.contains(tableNumber)) {
            throw new RuntimeException("该桌号已被占用");
        }
        
        // 更新订单桌号
        orderMapper.updateTableNumber(id, tableNumber);
    }
} 