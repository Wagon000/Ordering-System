<!--
 * Restaurant Ordering System - Main App Component
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 -->
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from './stores/user'
import { useCartStore } from './stores/cart'
import { ElMessageBox } from 'element-plus'
import {
  Food,
  ShoppingCart,
  List,
  User,
  ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

// 计算属性
const isLoggedIn = computed(() => userStore.isLoggedIn)
const username = computed(() => userStore.username)
const cartCount = computed(() => cartStore.count)

// 处理登出
const handleLogout = () => {
  ElMessageBox.confirm(
    '确定要退出登录吗?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    userStore.logout()
    router.push('/login')
  }).catch(() => {})
}

// 组件挂载时获取用户信息
onMounted(() => {
  if (userStore.isLoggedIn) {
    userStore.fetchUserInfo()
  }
})

const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      // 跳转到个人信息页面
      router.push('/profile')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm(
          '确定要退出登录吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        userStore.logout()
        router.push('/login')
      } catch {
        // 用户取消退出
      }
      break
  }
}
</script>

<template>
  <el-container class="app-wrapper">
    <el-header class="app-header" height="60px">
      <div class="header-content">
        <div class="logo" @click="router.push('/')">
          <h1>点餐系统</h1>
        </div>
        
        <div class="nav-menu">
          <template v-if="userStore.isLoggedIn">
            <!-- 管理员菜单 -->
            <template v-if="userStore.userInfo.role === -1">
              <el-menu
                mode="horizontal"
                :router="true"
                :default-active="route.path"
              >
                <el-menu-item index="/admin/food">
                  <el-icon><Food /></el-icon>
                  菜品管理
                </el-menu-item>
                <el-menu-item index="/admin/orders">
                  <el-icon><List /></el-icon>
                  订单管理
                </el-menu-item>
                <el-menu-item index="/admin/users">
                  <el-icon><User /></el-icon>
                  用户管理
                </el-menu-item>
              </el-menu>
            </template>
            
            <!-- 普通用户菜单 -->
            <template v-else>
              <el-menu
                mode="horizontal"
                :router="true"
                :default-active="route.path"
              >
                <el-menu-item index="/food">
                  <el-icon><Food /></el-icon>
                  点餐
                </el-menu-item>
                <el-menu-item index="/cart">
                  <el-icon><ShoppingCart /></el-icon>
                  购物车
                  <el-badge
                    v-if="cartStore.count > 0"
                    :value="cartStore.count"
                    class="cart-badge"
                  />
                </el-menu-item>
                <el-menu-item index="/orders">
                  <el-icon><List /></el-icon>
                  我的订单
                </el-menu-item>
                <el-menu-item index="/profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-menu-item>
              </el-menu>
            </template>
          </template>
        </div>
        
        <div class="user-menu">
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                {{ userStore.userInfo.usName }}
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="text" @click="router.push('/login')">登录</el-button>
            <el-button type="text" @click="router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    
    <el-main class="app-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </el-main>
    
    <el-footer class="app-footer" height="60px">
      <div class="footer-content">
        <p>&copy; 2025 点餐系统 - 版权所有</p>
      </div>
    </el-footer>
  </el-container>
</template>

<style>
/* 全局样式 */
.app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  padding: 0;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo {
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo h1 {
  margin: 0;
  font-size: clamp(18px, 4vw, 24px);
  color: #409EFF;
}

.nav-menu {
  flex: 1;
  margin: 0 20px;
  overflow-x: auto;
  overflow-y: hidden;
}

.nav-menu::-webkit-scrollbar {
  height: 3px;
}

.el-menu {
  border-bottom: none;
  background-color: transparent;
}

.el-menu--horizontal {
  border-bottom: none !important;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 10px;
  white-space: nowrap;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #409EFF;
  font-size: clamp(14px, 3vw, 16px);
}

.cart-badge {
  margin-top: -2px;
  margin-left: 5px;
}

.app-main {
  flex: 1;
  padding: 0;
  background-color: #f5f7fa;
  position: relative;
  overflow-x: hidden;
}

.app-footer {
  background-color: #fff;
  border-top: 1px solid #e6e6e6;
  padding: 0;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: clamp(12px, 2.5vw, 14px);
}

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .header-content {
    padding: 0 10px;
  }

  .nav-menu {
    margin: 0 10px;
  }

  .el-menu--horizontal > .el-menu-item {
    padding: 0 10px;
  }

  .user-menu .el-button {
    padding: 8px 12px;
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .app-header {
    background-color: #1a1a1a;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  }

  .app-main {
    background-color: #141414;
  }

  .app-footer {
    background-color: #1a1a1a;
    border-top-color: #2c2c2c;
  }

  .el-menu {
    background-color: transparent;
  }

  .el-menu--horizontal > .el-menu-item {
    color: #e0e0e0;
  }

  .el-menu--horizontal > .el-menu-item.is-active {
    color: #409EFF;
    border-bottom-color: #409EFF;
  }
}
</style>
