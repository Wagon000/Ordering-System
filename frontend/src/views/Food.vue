/**
 * Restaurant Ordering System - Food Menu View
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="food-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>菜品列表</h2>
          <div class="header-controls">
            <!-- 排序下拉菜单 -->
            <el-select 
              v-model="sortOption" 
              placeholder="价格排序" 
              style="margin-right: 15px; width: 120px"
              @change="handleSortChange"
            >
              <el-option label="默认排序" value="default" />
              <el-option label="价格从低到高" value="priceAsc" />
              <el-option label="价格从高到低" value="priceDesc" />
            </el-select>
            
            <!-- 搜索框 -->
            <el-input
              v-model="searchQuery"
              placeholder="搜索菜品..."
              prefix-icon="Search"
              clearable
              @clear="handleSearch"
              @input="handleSearch"
              style="width: 300px"
            />
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col 
        v-for="food in sortedFoodList" 
        :key="food.id" 
        :xs="24" 
        :sm="12" 
        :md="8" 
        :lg="6"
      >
        <el-card class="food-card" :body-style="{ padding: '0px' }">
          <el-image 
            :src="food.photo ? `http://localhost:8082/api${food.photo}` : '/placeholder.png'"
            class="food-image"
            fit="cover"
          />
          <div class="food-info">
            <h3>{{ food.fdName }}</h3>
            <p class="food-intro">{{ food.intro }}</p>
            <div class="food-price">
              <span class="price">¥{{ formatPrice(food.price) }}</span>
              <el-button 
                type="primary" 
                size="small" 
                :icon="Plus"
                circle
                @click="addToCart(food)"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="3" animated />
    </div>

    <!-- 空状态 -->
    <el-empty
      v-if="!loading && sortedFoodList.length === 0"
      description="暂无菜品"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { foodApi } from '../api'
import { useCartStore } from '../stores/cart'

const cartStore = useCartStore()
const loading = ref(true)
const foodList = ref([])
const searchQuery = ref('')
const sortOption = ref('default') // 默认排序选项

// 过滤后的菜品列表
const filteredFoodList = computed(() => {
  if (!searchQuery.value) return foodList.value
  const query = searchQuery.value.toLowerCase()
  return foodList.value.filter(food => 
    food.fdName.toLowerCase().includes(query) ||
    food.intro.toLowerCase().includes(query)
  )
})

// 排序后的菜品列表
const sortedFoodList = computed(() => {
  const filtered = filteredFoodList.value
  
  if (sortOption.value === 'default') {
    return filtered
  } else if (sortOption.value === 'priceAsc') {
    // 价格从低到高
    return [...filtered].sort((a, b) => a.price - b.price)
  } else if (sortOption.value === 'priceDesc') {
    // 价格从高到低
    return [...filtered].sort((a, b) => b.price - a.price)
  }
  
  return filtered
})

// 获取菜品列表
const fetchFoodList = async () => {
  loading.value = true
  try {
    const response = await foodApi.getFoodList()
    foodList.value = response.data.filter(food => food.fdStatus === 1) // 只显示上架的菜品
  } catch (error) {
    console.error('获取菜品列表失败:', error)
    ElMessage.error('获取菜品列表失败')
  } finally {
    loading.value = false
  }
}

// 添加到购物车
const addToCart = (food) => {
  // 创建一个新对象，避免修改原始数据
  const foodItem = { ...food }
  // 将价格从"分"转换为"元"
  foodItem.price = Number(formatPrice(food.price))
  cartStore.addItem(foodItem)
}

// 搜索处理
const handleSearch = () => {
  // 搜索逻辑已通过计算属性实现
}

// 排序处理
const handleSortChange = () => {
  // 排序逻辑已通过计算属性实现
}

// 格式化价格
const formatPrice = (price) => {
  return (price / 100).toFixed(2)
}

// 组件挂载时获取菜品列表
onMounted(() => {
  fetchFoodList()
})
</script>

<style scoped>
.food-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.header-controls {
  display: flex;
  align-items: center;
}

.food-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.food-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

.food-image {
  width: 100%;
  height: 200px;
  display: block;
}

.food-info {
  padding: 14px;
}

.food-info h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.food-intro {
  margin: 8px 0;
  font-size: 14px;
  color: #909399;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.food-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
}

.loading-container {
  padding: 20px;
}

/* 响应式布局调整 */
@media screen and (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .header-controls {
    margin-top: 15px;
    width: 100%;
  }
  
  .el-select {
    margin-bottom: 10px;
  }
}
</style> 