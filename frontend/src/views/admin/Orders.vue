/**
 * Restaurant Ordering System - Admin Orders Management
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="page-container orders-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>订单管理</h2>
          <div class="header-controls">
            <!-- 订单状态筛选 -->
            <el-select
              v-model="filterStatus"
              placeholder="订单状态"
              clearable
              style="width: 120px; margin-right: 15px;"
              @change="handleFilter"
            >
              <el-option label="全部" value="" />
              <el-option label="未完成" :value="-1" />
              <el-option label="已完成" :value="1" />
            </el-select>
            
            <!-- 订单类型筛选 -->
            <el-select
              v-model="filterType"
              placeholder="订单类型"
              clearable
              style="width: 120px; margin-right: 15px;"
              @change="handleFilter"
            >
              <el-option label="全部" value="" />
              <el-option label="堂食" :value="1" />
              <el-option label="外卖" :value="2" />
            </el-select>
            
            <!-- 搜索框 -->
            <el-input
              v-model="searchQuery"
              placeholder="搜索订单号、电话或地址..."
              prefix-icon="Search"
              clearable
              @input="handleSearch"
              style="width: 250px"
            />
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 订单列表 -->
    <el-table
      v-loading="loading"
      :data="filteredOrderList"
      style="width: 100%"
    >
      <el-table-column prop="id" label="订单号" width="100" />
      
      <el-table-column label="下单时间" width="160">
        <template #default="{ row }">
          {{ formatDate(row.time) }}
        </template>
      </el-table-column>
      
      <el-table-column label="订单类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.orderType === 1 ? 'success' : 'warning'">
            {{ row.orderType === 1 ? '堂食' : '外卖' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="桌号/地址" min-width="200">
        <template #default="{ row }">
          <template v-if="row.orderType === 1">
            <span v-if="row.orStatus === -1">
              <el-select 
                v-model="row.tableNumber"
                placeholder="选择桌号"
                style="width: 120px"
                @change="(value) => handleTableChange(row, value)"
              >
                <el-option
                  v-for="i in tableCount"
                  :key="i"
                  :label="`${i}号桌`"
                  :value="i"
                  :disabled="isTableOccupied(i, row.id)"
                />
              </el-select>
            </span>
            <span v-else>{{ row.tableNumber }}号桌</span>
          </template>
          <template v-else>
            {{ row.address }}
          </template>
        </template>
      </el-table-column>

      <el-table-column prop="phone" label="联系电话" width="120" />
      
      <el-table-column label="订单金额" width="120">
        <template #default="{ row }">
          <span class="price">¥{{ formatPrice(row.sum) }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="订单状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getOrderStatusType(row.orStatus)">
            {{ getOrderStatusText(row.orStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="评分" width="120">
        <template #default="{ row }">
          <el-rate
            v-if="row.star !== -1"
            v-model="row.star"
            disabled
            show-score
            text-color="#ff9900"
          />
          <span v-else>未评分</span>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button
            type="primary"
            size="small"
            @click="handleViewDetails(row)"
          >
            查看详情
          </el-button>
          <el-button
            v-if="row.orStatus === -1"
            type="success"
            size="small"
            @click="handleComplete(row)"
          >
            完成订单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="订单详情"
      width="600px"
    >
      <template v-if="currentOrder">
        <div class="order-info">
          <p><strong>订单号：</strong>{{ currentOrder.id }}</p>
          <p><strong>下单时间：</strong>{{ formatDate(currentOrder.time) }}</p>
          <p><strong>订单类型：</strong>{{ currentOrder.orderType === 1 ? '堂食' : '外卖' }}</p>
          <p v-if="currentOrder.orderType === 1">
            <strong>桌号：</strong>{{ currentOrder.tableNumber }}号桌
          </p>
          <p v-else>
            <strong>送餐地址：</strong>{{ currentOrder.address }}
          </p>
          <p><strong>联系电话：</strong>{{ currentOrder.phone }}</p>
          <p><strong>订单状态：</strong>
            <el-tag :type="getOrderStatusType(currentOrder.orStatus)">
              {{ getOrderStatusText(currentOrder.orStatus) }}
            </el-tag>
          </p>
          <p v-if="currentOrder.remark">
            <strong>备注：</strong>{{ currentOrder.remark }}
          </p>
        </div>

        <el-table :data="currentOrder.foodLists" style="width: 100%">
          <el-table-column label="菜品" prop="fdName" />
          <el-table-column label="单价" prop="price">
            <template #default="{ row }">
              <span class="price">¥{{ formatPrice(row.price) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数量" prop="count" width="120" />
          <el-table-column label="小计" width="120">
            <template #default="{ row }">
              <span class="price">¥{{ formatPrice(row.sum) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <div class="order-total">
          <template v-if="currentOrder.orderType === 2">
            <div class="total-item">
              <span>商品总额：</span>
              <span class="price">¥{{ formatPrice(currentOrder.sum - deliveryFee * 100) }}</span>
            </div>
            <div class="total-item">
              <span>配送费：</span>
              <span class="price">¥{{ formatPrice(deliveryFee * 100) }}</span>
            </div>
          </template>
          <div class="total-item final">
            <span>实付金额：</span>
            <span class="price">¥{{ formatPrice(currentOrder.sum) }}</span>
          </div>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderApi, settingApi } from '../../api'

const loading = ref(false)
const dialogVisible = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const orderList = ref([])
const currentOrder = ref(null)
const tableCount = ref(20)
const deliveryFee = ref(5)

// 搜索和筛选状态
const searchQuery = ref('')
const filterStatus = ref('')
const filterType = ref('')

// 过滤后的订单列表
const filteredOrderList = computed(() => {
  let result = orderList.value

  // 状态筛选
  if (filterStatus.value !== '') {
    result = result.filter(order => order.orStatus === filterStatus.value)
  }

  // 类型筛选
  if (filterType.value !== '') {
    result = result.filter(order => order.orderType === filterType.value)
  }

  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(order => 
      order.id.toString().includes(query) ||
      (order.phone && order.phone.includes(query)) ||
      (order.address && order.address.toLowerCase().includes(query))
    )
  }

  return result
})

// 获取订单列表
const fetchOrderList = async () => {
  loading.value = true
  try {
    const response = await orderApi.getAllOrders(currentPage.value, pageSize.value)
    orderList.value = response.data.rows
    total.value = response.data.totalCount
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 获取系统设置
const fetchSettings = async () => {
  try {
    const response = await settingApi.getSettings()
    const settings = response.data
    tableCount.value = Number(settings.find(s => s.setting_key === 'table_count')?.setting_value || 20)
    deliveryFee.value = Number(settings.find(s => s.setting_key === 'delivery_fee')?.setting_value || 5)
  } catch (error) {
    console.error('获取系统设置失败:', error)
  }
}

// 检查桌号是否被占用
const isTableOccupied = (tableNumber, currentOrderId) => {
  return orderList.value.some(order => 
    order.id !== currentOrderId && 
    order.orderType === 1 && 
    order.orStatus === -1 && 
    order.tableNumber === tableNumber
  )
}

// 处理桌号变更
const handleTableChange = async (order, tableNumber) => {
  try {
    await orderApi.updateTableNumber(order.id, tableNumber)
    ElMessage.success('桌号已更新')
  } catch (error) {
    console.error('更新桌号失败:', error)
    ElMessage.error('更新桌号失败')
    // 恢复原桌号
    order.tableNumber = order.originalTableNumber
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
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
  if (price === null || price === undefined) return '0.00'
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

// 查看订单详情
const handleViewDetails = async (order) => {
  try {
    const response = await orderApi.getOrderDetail(order.id)
    currentOrder.value = {
      ...order,
      foodLists: response.data.foodLists
    }
    dialogVisible.value = true
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  }
}

// 完成订单
const handleComplete = (order) => {
  ElMessageBox.confirm(
    `确定要将订单 #${order.id} 标记为已完成吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await orderApi.updateOrderStatus(order.id, 1)
      ElMessage.success('订单已完成')
      order.orStatus = 1
    } catch (error) {
      console.error('更新订单状态失败:', error)
      ElMessage.error('更新订单状态失败')
    }
  }).catch(() => {})
}

// 处理页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrderList()
}

// 处理每页数量改变
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
  fetchOrderList()
}

// 处理搜索
const handleSearch = debounce(() => {
  currentPage.value = 1 // 重置到第一页
  fetchOrderList()
}, 300)

// 处理筛选
const handleFilter = () => {
  currentPage.value = 1 // 重置到第一页
  fetchOrderList()
}

// 防抖函数
function debounce(fn, delay) {
  let timer = null
  return function (...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => fn.apply(this, args), delay)
  }
}

// 组件挂载时获取数据
onMounted(() => {
  fetchSettings()
  fetchOrderList()
})
</script>

<style scoped>
.orders-container {
  padding: clamp(10px, 3vw, 20px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-controls {
  display: flex;
  align-items: center;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: clamp(20px, 4vw, 24px);
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.order-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.order-info p {
  margin: 5px 0;
}

.order-total {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.total-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-size: 14px;
}

.total-item.final {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px dashed #ebeef5;
  font-size: 16px;
  font-weight: bold;
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .order-info {
    background-color: #2b2b2b;
  }

  .order-total {
    border-top-color: #4c4c4c;
  }

  .total-item.final {
    border-top-color: #4c4c4c;
  }
}
</style> 