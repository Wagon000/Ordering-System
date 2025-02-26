/**
 * Restaurant Ordering System - Home View
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="welcome-section">
          <h1>欢迎使用点餐系统</h1>
          <p v-if="userStore.isLoggedIn">{{ userStore.userInfo.role === -1 ? '管理员' : '用户' }}，{{ userStore.userInfo.usName }}</p>
          <p v-else>请先登录系统</p>
        </div>
      </el-col>
    </el-row>
    
    <!-- 未登录显示 -->
    <el-row v-if="!userStore.isLoggedIn" :gutter="20">
      <el-col :span="24" class="action-buttons">
        <el-button type="primary" @click="router.push('/login')">登录</el-button>
        <el-button @click="router.push('/register')">注册</el-button>
      </el-col>
    </el-row>

    <!-- 管理员功能区 -->
    <template v-else-if="userStore.userInfo.role === -1">
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item, index) in adminFeatures" :key="index">
          <el-card class="feature-card">
            <div class="feature-icon">
              <el-icon><component :is="item.icon"></component></el-icon>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.description }}</p>
            <el-button type="primary" @click="router.push(item.route)">
              {{ item.buttonText }}
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 普通用户功能区 -->
    <template v-else>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item, index) in userFeatures" :key="index">
          <el-card class="feature-card">
            <div class="feature-icon">
              <el-icon><component :is="item.icon"></component></el-icon>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.description }}</p>
            <el-button type="primary" @click="router.push(item.route)">
              {{ item.buttonText }}
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import {
  Food,
  ShoppingCart,
  List,
  User,
  Setting
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 管理员功能列表
const adminFeatures = [
  {
    icon: Food,
    title: '菜品管理',
    description: '管理系统中的菜品信息',
    route: '/admin/food',
    buttonText: '管理菜品'
  },
  {
    icon: List,
    title: '订单管理',
    description: '查看和处理用户订单',
    route: '/admin/orders',
    buttonText: '查看订单'
  },
  {
    icon: User,
    title: '用户管理',
    description: '管理系统用户信息',
    route: '/admin/users',
    buttonText: '管理用户'
  }
]

// 普通用户功能列表
const userFeatures = [
  {
    icon: Food,
    title: '浏览菜品',
    description: '查看所有可用的菜品',
    route: '/food',
    buttonText: '去点餐'
  },
  {
    icon: ShoppingCart,
    title: '我的购物车',
    description: '查看已选择的菜品',
    route: '/cart',
    buttonText: '查看购物车'
  },
  {
    icon: List,
    title: '我的订单',
    description: '查看历史订单信息',
    route: '/orders',
    buttonText: '查看订单'
  }
]
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  text-align: center;
  margin-bottom: 40px;
}

.welcome-section h1 {
  color: #409EFF;
  margin-bottom: 10px;
}

.action-buttons {
  text-align: center;
  margin: 20px 0;
}

.feature-card {
  margin-bottom: 20px;
  text-align: center;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

.feature-icon {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 20px;
}

.feature-card h3 {
  margin: 10px 0;
  color: #303133;
}

.feature-card p {
  color: #606266;
  margin-bottom: 20px;
}
</style> 