/**
 * Restaurant Ordering System - Order Controller
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.controller;

import com.example.orderingsystem.common.Result;
import com.example.orderingsystem.entity.FoodList;
import com.example.orderingsystem.entity.Order;
import com.example.orderingsystem.entity.PageBean;
import com.example.orderingsystem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单Controller
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 添加订单
     */
    @PostMapping
    public Result<Order> add(@RequestBody Map<String, Object> params) {
        log.info("添加订单: {}", params);
        
        try {
            // 获取订单信息
            Order order = new Order();
            order.setUsId((Integer) params.get("userId"));
            order.setSum((Integer) params.get("sum"));
            
            // 设置订单类型和相关信息
            order.setOrderType((Integer) params.get("orderType"));
            order.setTableNumber((Integer) params.get("tableNumber"));
            order.setPhone((String) params.get("phone"));
            order.setAddress((String) params.get("address"));
            order.setRemark((String) params.get("remark"));
            
            // 获取订单明细并转换为FoodList对象
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> foodListsMap = (List<Map<String, Object>>) params.get("foodLists");
            List<FoodList> foodLists = new ArrayList<>();
            
            for (Map<String, Object> item : foodListsMap) {
                FoodList foodList = new FoodList();
                foodList.setFdId((Integer) item.get("fdId"));
                foodList.setFdName((String) item.get("fdName"));
                foodList.setPrice((Integer) item.get("price"));
                foodList.setCount((Integer) item.get("count"));
                foodList.setSum((Integer) item.get("sum"));
                foodLists.add(foodList);
            }
            
            // 添加订单
            Order newOrder = orderService.add(order, foodLists);
            return Result.success(newOrder);
        } catch (RuntimeException e) {
            log.error("添加订单失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加订单异常: {}", e.getMessage(), e);
            return Result.error("系统异常，请联系管理员");
        }
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageBean<Order>> selectByPage(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("分页查询订单: currentPage={}, pageSize={}", currentPage, pageSize);
        
        PageBean<Order> pageBean = orderService.selectByPage(currentPage, pageSize);
        return Result.success(pageBean);
    }
    
    /**
     * 根据用户ID查询订单
     */
    @GetMapping("/user/{userId}")
    public Result<List<Order>> selectByUserId(@PathVariable int userId) {
        log.info("查询用户订单: userId={}", userId);
        
        List<Order> orders = orderService.selectByUserId(userId);
        return Result.success(orders);
    }
    
    /**
     * 更新订单状态
     */
    @PutMapping("/status/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateStatus(@PathVariable int id, @PathVariable int status) {
        log.info("更新订单状态: id={}, status={}", id, status);
        
        orderService.updateStatus(id, status);
        return Result.success();
    }
    
    /**
     * 更新订单评分
     */
    @PutMapping("/star/{id}/{star}")
    public Result<Void> updateStar(@PathVariable int id, @PathVariable int star) {
        log.info("更新订单评分: id={}, star={}", id, star);
        
        orderService.updateStar(id, star);
        return Result.success();
    }
    
    /**
     * 查询订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable int orderId) {
        log.info("查询订单详情: orderId={}", orderId);
        
        // 查询订单明细
        List<FoodList> foodLists = orderService.selectFoodListByOrderId(orderId);
        
        // 封装返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("foodLists", foodLists);
        
        return Result.success(data);
    }
    
    /**
     * 获取已占用的桌号列表
     */
    @GetMapping("/occupied-tables")
    public Result<List<Integer>> getOccupiedTables() {
        log.info("获取已占用桌号列表");
        List<Integer> occupiedTables = orderService.getOccupiedTables();
        return Result.success(occupiedTables);
    }
    
    /**
     * 更新订单桌号
     */
    @PutMapping("/{id}/table")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateTableNumber(@PathVariable int id, @RequestBody Map<String, Integer> params) {
        log.info("更新订单桌号: orderId={}, params={}", id, params);
        
        Integer tableNumber = params.get("tableNumber");
        if (tableNumber == null || tableNumber < 1) {
            return Result.error("桌号必须大于0");
        }
        
        // 更新订单桌号
        orderService.updateTableNumber(id, tableNumber);
        return Result.success();
    }
} 