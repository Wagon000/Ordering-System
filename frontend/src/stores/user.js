/**
 * Restaurant Ordering System - User Store
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '../api'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
  const loading = ref(false)
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value.usName || '')
  
  // 动作
  const login = async (credentials) => {
    loading.value = true
    try {
      console.log('开始登录请求，凭据:', credentials)
      console.log('发送请求到后端API...')
      
      const response = await userApi.login(credentials)
      console.log('登录响应成功:', response)
      
      // 确保响应中包含token和user
      if (!response.data || !response.data.token || !response.data.user) {
        console.error('登录响应缺少必要数据:', response)
        throw new Error('登录响应数据不完整')
      }
      
      // 检查用户角色
      const userRole = response.data.user.role
      console.log('用户角色:', userRole)
      if (userRole === -1) {
        console.log('检测到管理员账户登录')
      } else {
        console.log('检测到普通用户登录')
      }
      
      token.value = response.data.token
      userInfo.value = response.data.user
      
      // 保存到本地存储
      localStorage.setItem('token', token.value)
      localStorage.setItem('user', JSON.stringify(userInfo.value))
      
      console.log('登录成功，用户信息已保存')
      return response
    } catch (error) {
      console.error('登录过程中发生错误:', error)
      
      // 详细记录错误信息
      if (error.response) {
        console.error('服务器响应错误:')
        console.error('- 状态码:', error.response.status)
        console.error('- 响应数据:', error.response.data)
        console.error('- 响应头:', error.response.headers)
      } else if (error.request) {
        console.error('请求已发送但没有收到响应:', error.request)
      } else {
        console.error('请求设置错误:', error.message)
      }
      
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const register = async (userData) => {
    loading.value = true
    try {
      const response = await userApi.register(userData)
      return response
    } finally {
      loading.value = false
    }
  }
  
  const logout = () => {
    // 清除状态
    token.value = ''
    userInfo.value = {}
    
    // 清除本地存储
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }
  
  const fetchUserInfo = async () => {
    // 如果没有token，则不获取
    if (!token.value) {
      return
    }
    
    loading.value = true
    try {
      console.log('正在获取用户信息...')
      const response = await userApi.getUserInfo()
      if (response.data) {
        console.log('获取到用户信息:', response.data)
        userInfo.value = response.data
        localStorage.setItem('user', JSON.stringify(userInfo.value))
      } else {
        // 如果没有获取到用户信息，清除登录状态
        console.error('获取用户信息失败: 服务器返回空数据')
        logout()
        throw new Error('获取用户信息失败')
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // 如果是401错误或者其他错误，清除登录状态
      logout()
      throw error
    } finally {
      loading.value = false
    }
  }
  
  return {
    token,
    userInfo,
    loading,
    isLoggedIn,
    username,
    login,
    register,
    logout,
    fetchUserInfo
  }
}) 