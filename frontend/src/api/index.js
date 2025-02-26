/**
 * Restaurant Ordering System - API Configuration
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8082/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回的状态码不是200，说明接口请求有误
    if (res.code !== 200) {
      console.error('API错误响应:', res)
      ElMessage.error(res.msg || '服务器错误')
      
      // 401: 未登录或token过期
      if (res.code === 401) {
        // 清除token
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        
        // 重定向到登录页
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      }
      
      return Promise.reject(new Error(res.msg || '服务器错误'))
    } else {
      return res
    }
  },
  error => {
    console.error('API响应错误:', error)
    
    // 获取详细的错误信息
    let errorMessage = '网络错误，请稍后再试'
    
    if (error.response) {
      // 服务器返回了错误状态码
      console.error('错误状态码:', error.response.status)
      console.error('错误数据:', error.response.data)
      
      if (error.response.data && error.response.data.msg) {
        errorMessage = error.response.data.msg
      } else if (error.response.status === 401) {
        errorMessage = '未授权，请重新登录'
        // 清除token
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        
        // 重定向到登录页
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      } else if (error.response.status === 403) {
        errorMessage = '禁止访问'
      } else if (error.response.status === 404) {
        errorMessage = '请求的资源不存在'
      } else if (error.response.status === 500) {
        errorMessage = '服务器内部错误'
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      errorMessage = '服务器无响应，请检查网络'
    }
    
    ElMessage.error(errorMessage)
    return Promise.reject(error)
  }
)

// 用户相关API
export const userApi = {
  // 用户登录
  login(data) {
    console.log('API发送登录请求详细信息:')
    console.log('请求数据:', data)
    console.log('请求URL:', api.defaults.baseURL + '/user/login')
    console.log('请求方法: POST')
    console.log('请求头:', api.defaults.headers)
    
    return api.post('/user/login', data)
      .then(response => {
        console.log('API登录响应成功:', response)
        return response
      })
      .catch(error => {
        console.error('API登录请求失败详细信息:')
        
        // 详细记录错误信息
        if (error.response) {
          console.error('服务器响应错误:')
          console.error('- 状态码:', error.response.status)
          console.error('- 响应数据:', error.response.data)
          console.error('- 响应头:', error.response.headers)
          
          // 特别处理401错误
          if (error.response.status === 401) {
            console.error('认证失败，可能是用户名或密码错误')
          } else if (error.response.status === 500) {
            console.error('服务器内部错误，可能是后端处理问题')
          }
        } else if (error.request) {
          console.error('请求已发送但没有收到响应:', error.request)
        } else {
          console.error('请求设置错误:', error.message)
        }
        
        throw error
      })
  },
  
  // 用户注册
  register(data) {
    return api.post('/user/register', data)
  },
  
  // 获取用户信息
  getUserInfo() {
    return api.get('/user/info')
  },
  
  // 更新用户信息
  updateUserInfo(data) {
    return api.put('/user', data)
  },
  
  // 修改密码
  changePassword(data) {
    return api.put('/user/password', data)
  },
  
  // 获取用户列表（分页）
  getUserList(currentPage = 1, pageSize = 10) {
    return api.get('/user/page', {
      params: { currentPage, pageSize }
    })
  },
  
  // 删除用户
  deleteUser(id) {
    return api.delete(`/user/${id}`)
  },
  
  // 更新用户信息
  updateUser(data) {
    return api.put('/user', data)
  }
}

// 食品相关API
export const foodApi = {
  // 获取食品列表
  getFoodList() {
    return api.get('/food')
  },
  
  // 获取食品详情
  getFoodDetail(id) {
    return api.get(`/food/${id}`)
  },

  // 更新食品状态
  updateFoodStatus(id, status) {
    return api.put(`/food/status/${id}/${status}`)
  },

  // 删除食品
  deleteFood(id) {
    return api.delete(`/food/${id}`)
  },

  // 更新食品信息
  updateFood(food) {
    return api.put('/food', food)
  },

  // 添加食品
  addFood(food) {
    return api.post('/food', food)
  }
}

// 订单相关API
export const orderApi = {
  // 创建订单
  createOrder(data) {
    // 确保数据格式正确
    const orderData = {
      userId: data.userId,
      sum: data.sum,
      orderType: data.orderType,
      tableNumber: data.tableNumber,
      address: data.address,
      phone: data.phone,
      remark: data.remark,
      foodLists: data.foodLists
    }
    
    console.log('提交订单数据:', orderData)
    return api.post('/order', orderData)
  },
  
  // 获取当前用户的订单列表
  getOrderList() {
    // 从localStorage获取用户信息
    const userInfo = JSON.parse(localStorage.getItem('user') || '{}')
    if (!userInfo.id) {
      return Promise.reject(new Error('用户未登录'))
    }
    return api.get(`/order/user/${userInfo.id}`)
  },
  
  // 管理员获取所有订单列表
  getAllOrders(currentPage = 1, pageSize = 10) {
    return api.get('/order/page', {
      params: { currentPage, pageSize }
    })
  },
  
  // 获取订单详情
  getOrderDetail(id) {
    return api.get(`/order/detail/${id}`)
  },

  // 获取已占用桌号列表
  getOccupiedTables() {
    return api.get('/order/occupied-tables')
  },

  // 修改订单桌号（管理员）
  updateTableNumber(orderId, tableNumber) {
    return api.put(`/order/${orderId}/table`, { tableNumber })
  },

  // 更新订单状态
  updateOrderStatus(orderId, status) {
    return api.put(`/order/status/${orderId}/${status}`)
  },

  // 更新订单评分
  updateOrderStar(orderId, star) {
    return api.put(`/order/star/${orderId}/${star}`)
  }
}

// 系统设置相关API（管理员）
export const settingApi = {
  // 获取系统设置
  getSettings() {
    return api.get('/settings')
  },

  // 更新桌号数量
  updateTableCount(count) {
    return api.put('/settings/table-count', { count })
  },

  // 更新配送费
  updateDeliveryFee(fee) {
    return api.put('/settings/delivery-fee', { fee })
  }
}

export default api 