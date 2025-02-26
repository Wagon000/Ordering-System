/**
 * Restaurant Ordering System - Food Controller
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
package com.example.orderingsystem.controller;

import com.example.orderingsystem.common.Result;
import com.example.orderingsystem.entity.Food;
import com.example.orderingsystem.entity.PageBean;
import com.example.orderingsystem.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * 食品Controller
 */
@Slf4j
@RestController
@RequestMapping("/food")
public class FoodController {
    
    @Value("${resource.image-upload-path}")
    private String imageUploadPath;
    
    @Autowired
    private FoodService foodService;
    
    /**
     * 查询所有食品
     */
    @GetMapping
    public Result<List<Food>> selectAll() {
        log.info("查询所有食品");
        
        List<Food> foods = foodService.selectAll();
        return Result.success(foods);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<PageBean<Food>> selectByPage(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("分页查询食品: currentPage={}, pageSize={}", currentPage, pageSize);
        
        PageBean<Food> pageBean = foodService.selectByPage(currentPage, pageSize);
        return Result.success(pageBean);
    }
    
    /**
     * 添加食品
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> add(@RequestBody Food food) {
        log.info("添加食品: {}", food.getFdName());
        
        foodService.add(food);
        return Result.success();
    }
    
    /**
     * 上传食品图片
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("上传食品图片");
        
        // 判断文件是否为空
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        
        try {
            // 获取项目根目录
            String projectPath = System.getProperty("user.dir");
            
            // 构建完整的上传路径
            String uploadPath = projectPath + File.separator + imageUploadPath;
            log.info("上传路径: {}", uploadPath);
            
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    log.error("创建上传目录失败");
                    return Result.error("创建上传目录失败");
                }
            }
            
            // 获取文件名和后缀
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return Result.error("文件名不能为空");
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只能上传图片文件");
            }
            
            // 获取文件后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!".jpg".equals(suffix) && !".jpeg".equals(suffix) && !".png".equals(suffix) && !".gif".equals(suffix)) {
                return Result.error("只支持jpg、jpeg、png、gif格式的图片");
            }
            
            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString() + suffix;
            
            // 构建完整的文件路径
            Path filePath = Paths.get(uploadPath, fileName);
            
            // 保存文件
            Files.copy(file.getInputStream(), filePath);
            
            // 返回可访问的URL路径
            String accessPath = "/imgs/" + fileName;
            log.info("文件访问路径: {}", accessPath);
            
            return Result.success(accessPath);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return Result.error("上传文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除图片文件
     */
    private void deleteImageFile(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }
        
        try {
            // 获取文件名
            String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            
            // 构建完整的文件路径
            String projectPath = System.getProperty("user.dir");
            Path filePath = Paths.get(projectPath, imageUploadPath, fileName);
            
            // 删除文件
            Files.deleteIfExists(filePath);
            log.info("已删除图片文件: {}", filePath);
        } catch (IOException e) {
            log.error("删除图片文件失败: {}", e.getMessage());
        }
    }
    
    /**
     * 根据ID删除食品
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteById(@PathVariable int id) {
        log.info("删除食品: id={}", id);
        
        // 获取食品信息
        Food food = foodService.selectById(id);
        if (food != null) {
            // 删除关联的图片文件
            deleteImageFile(food.getPhoto());
        }
        
        foodService.deleteById(id);
        return Result.success();
    }
    
    /**
     * 根据ID查询食品
     */
    @GetMapping("/{id}")
    public Result<Food> selectById(@PathVariable int id) {
        log.info("查询食品: id={}", id);
        
        Food food = foodService.selectById(id);
        if (food != null) {
            return Result.success(food);
        }
        
        return Result.error("食品不存在");
    }
    
    /**
     * 更新食品信息
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@RequestBody Food food) {
        log.info("更新食品: id={}", food.getId());
        
        // 获取旧的食品信息
        Food oldFood = foodService.selectById(food.getId());
        if (oldFood != null && !oldFood.getPhoto().equals(food.getPhoto())) {
            // 如果图片发生变化，删除旧图片
            deleteImageFile(oldFood.getPhoto());
        }
        
        foodService.update(food);
        return Result.success();
    }
    
    /**
     * 更新食品状态
     */
    @PutMapping("/status/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateStatus(@PathVariable int id, @PathVariable int status) {
        log.info("更新食品状态: id={}, status={}", id, status);
        
        foodService.updateStatus(id, status);
        return Result.success();
    }
} 