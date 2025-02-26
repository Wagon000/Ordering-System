/**
 * Restaurant Ordering System - Order History View
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="order-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>我的订单</h2>
        </div>
      </el-col>
    </el-row>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="3" animated />
    </div>

    <!-- 订单列表 -->
    <template v-else>
      <el-empty
        v-if="orders.length === 0"
        description="暂无订单"
      >
        <el-button type="primary" @click="router.push('/food')">
          去点餐
        </el-button>
      </el-empty>

      <div v-else class="order-list">
        <el-card v-for="order in orders" :key="order.id" class="order-card">
          <template #header>
            <div class="order-header">
              <div class="order-info">
                <span class="order-number">订单号: {{ order.id }}</span>
                <span class="order-time">{{ formatDate(order.time) }}</span>
              </div>
              <div class="order-status">
                <el-tag :type="getOrderStatusType(order.orStatus)">
                  {{ getOrderStatusText(order.orStatus) }}
                </el-tag>
              </div>
            </div>
          </template>

          <!-- 订单类型和位置信息 -->
          <div class="order-type-info">
            <el-tag size="small" :type="order.orderType === 1 ? 'success' : 'warning'" class="order-type-tag">
              {{ order.orderType === 1 ? '堂食' : '外卖' }}
            </el-tag>
            <span v-if="order.orderType === 1 && order.tableNumber" class="location-info">
              <el-icon><Location /></el-icon> 桌号: {{ order.tableNumber }}
            </span>
            <span v-else-if="order.orderType === 2 && order.address" class="location-info">
              <el-icon><Location /></el-icon> 地址: {{ order.address }}
            </span>
            <span v-if="order.phone" class="phone-info">
              <el-icon><Phone /></el-icon> 电话: {{ order.phone }}
            </span>
          </div>

          <!-- 订单详情 -->
          <el-table :data="order.foodLists" style="width: 100%">
            <el-table-column label="菜品" prop="fdName" />
            <el-table-column label="单价" prop="price">
              <template #default="{ row }">
                <span class="price">¥{{ formatPrice(row.price) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数量" prop="count" width="100" />
            <el-table-column label="小计" width="120">
              <template #default="{ row }">
                <span class="price">¥{{ formatPrice(row.sum) }}</span>
              </template>
            </el-table-column>
          </el-table>

          <div class="order-footer">
            <div class="order-total">
              总计: <span class="price">¥{{ formatPrice(order.sum) }}</span>
            </div>
            
            <!-- 订单操作 -->
            <div class="order-actions">
              <!-- 未评分且已完成的订单可以评分 -->
              <el-rate
                v-if="order.orStatus === 1 && order.star === -1"
                v-model="order.tempStar"
                :texts="['很差', '较差', '一般', '不错', '很好']"
                show-text
                @change="handleRate(order)"
              />
              <!-- 已评分的订单显示评分 -->
              <el-rate
                v-else-if="order.star !== -1"
                v-model="order.star"
                disabled
                show-score
                text-color="#ff9900"
              />
            </div>
          </div>
        </el-card>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { orderApi } from '../api'
import { ElMessage } from 'element-plus'
import { Location, Phone } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(true)
const orders = ref([])

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const response = await orderApi.getOrderList()
    orders.value = response.data
    
    // 为每个订单添加临时评分字段
    orders.value.forEach(order => {
      order.tempStar = 0
    })
    
    // 获取每个订单的详情
    await Promise.all(orders.value.map(async (order) => {
      try {
        const detailResponse = await orderApi.getOrderDetail(order.id)
        order.foodLists = detailResponse.data.foodLists
      } catch (error) {
        console.error(`获取订单${order.id}详情失败:`, error)
        order.foodLists = []
      }
    }))
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化价格
const formatPrice = (price) => {
  return (price / 100).toFixed(2)
}

// 获取订单状态类型
const getOrderStatusType = (status) => {
  switch (status) {
    case -1:
      return 'warning'
    case 1:
      return 'success'
    default:
      return 'info'
  }
}

// 获取订单状态文本
const getOrderStatusText = (status) => {
  switch (status) {
    case -1:
      return '未完成'
    case 1:
      return '已完成'
    default:
      return '未知状态'
  }
}

// 处理评分
const handleRate = async (order) => {
  try {
    await orderApi.updateOrderStar(order.id, order.tempStar)
    order.star = order.tempStar
    ElMessage.success('评分成功')
  } catch (error) {
    console.error('评分失败:', error)
    ElMessage.error('评分失败')
    order.tempStar = 0
  }
}

// 组件挂载时获取订单列表
onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.order-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.loading-container {
  padding: 20px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  margin-bottom: 0;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-info {
  display: flex;
  gap: 20px;
}

.order-number {
  font-weight: bold;
  color: #303133;
}

.order-time {
  color: #909399;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.order-footer {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.order-total {
  font-size: 16px;
}

.order-total .price {
  font-size: 20px;
  margin-left: 5px;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.order-type-info {
  margin: 10px 0 15px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 15px;
  color: #606266;
  font-size: 14px;
}

.order-type-tag {
  margin-right: 5px;
}

.location-info, .phone-info {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style> 