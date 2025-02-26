<template>
  <div class="page-container cart-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>我的购物车</h2>
          <el-button 
            type="danger" 
            :disabled="cartStore.isEmpty"
            @click="handleClearCart"
          >
            清空购物车
          </el-button>
        </div>
      </el-col>
    </el-row>

    <!-- 购物车为空 -->
    <el-empty
      v-if="cartStore.isEmpty"
      description="购物车是空的"
    >
      <el-button type="primary" @click="router.push('/food')">
        去点餐
      </el-button>
    </el-empty>

    <!-- 购物车列表 -->
    <template v-else>
      <el-card class="cart-content">
        <el-table :data="cartStore.items" style="width: 100%">
          <el-table-column label="菜品" min-width="300">
            <template #default="{ row }">
              <div class="food-info">
                <el-image
                  :src="row.photo ? `${apiBaseUrl}${row.photo}` : '/placeholder.png'"
                  class="food-image"
                  fit="cover"
                />
                <div class="food-details">
                  <h3>{{ row.fdName }}</h3>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column label="单价" width="120">
            <template #default="{ row }">
              <span class="price">¥{{ formatPrice(row.price) }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="数量" width="200">
            <template #default="{ row }">
              <el-input-number
                v-model="row.quantity"
                :min="0"
                :max="99"
                @change="(value) => handleQuantityChange(row.id, value, row)"
              />
            </template>
          </el-table-column>
          
          <el-table-column label="小计" width="120">
            <template #default="{ row }">
              <span class="price">¥{{ formatPrice(row.price * row.quantity) }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button
                type="danger"
                size="small"
                @click="handleRemoveItem(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 结算区域 -->
        <div class="checkout-section">
          <div class="total-info">
            <span>总计:</span>
            <span class="total-price">¥{{ formatPrice(cartStore.total) }}</span>
          </div>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleCheckout"
          >
            结算
          </el-button>
        </div>
      </el-card>
    </template>

    <!-- 结算对话框 -->
    <el-dialog
      v-model="checkoutDialogVisible"
      title="订单确认"
      width="500px"
    >
      <el-form
        ref="checkoutFormRef"
        :model="checkoutForm"
        :rules="checkoutRules"
        label-width="100px"
      >
        <el-form-item label="订单类型" prop="orderType">
          <el-radio-group v-model="checkoutForm.orderType">
            <el-radio :label="1">堂食</el-radio>
            <el-radio :label="2">外卖</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 堂食选项 -->
        <template v-if="checkoutForm.orderType === 1">
          <el-form-item label="桌号" prop="tableNumber">
            <el-select 
              v-model="checkoutForm.tableNumber" 
              placeholder="请选择桌号"
              style="width: 100%"
            >
              <el-option
                v-for="i in tableCount"
                :key="i"
                :label="`${i}号桌`"
                :value="i"
                :disabled="occupiedTables.includes(i)"
              />
            </el-select>
          </el-form-item>
        </template>

        <!-- 外卖选项 -->
        <template v-if="checkoutForm.orderType === 2">
          <el-form-item label="送餐地址" prop="address">
            <el-input
              v-model="checkoutForm.address"
              type="textarea"
              :rows="2"
              placeholder="请输入详细送餐地址"
            />
          </el-form-item>
        </template>

        <!-- 共同信息 -->
        <el-form-item label="联系电话" prop="phone">
          <el-input
            v-model="checkoutForm.phone"
            placeholder="请输入联系电话"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="checkoutForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息（选填）"
          />
        </el-form-item>
      </el-form>

      <!-- 订单金额信息 -->
      <div class="checkout-info">
        <div class="info-item">
          <span>商品总额：</span>
          <span class="price">¥{{ formatPrice(cartStore.total) }}</span>
        </div>
        <div class="info-item" v-if="checkoutForm.orderType === 2">
          <span>配送费：</span>
          <span class="price">¥{{ deliveryFee }}</span>
        </div>
        <div class="info-item total">
          <span>实付金额：</span>
          <span class="price">¥{{ formatPrice(finalTotal) }}</span>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="checkoutDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitOrder" :loading="loading">
            确认下单
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'
import { useUserStore } from '../stores/user'
import { orderApi } from '../api'
import { ElMessageBox, ElMessage } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()
const loading = ref(false)
const checkoutDialogVisible = ref(false)
const checkoutFormRef = ref(null)

// 配送费
const deliveryFee = 5

// 可用桌位数量（这个值应该从后端获取）
const tableCount = 20

// 已占用的桌号（这个应该从后端获取）
const occupiedTables = ref([])

// 在script部分添加API基础URL
const apiBaseUrl = 'http://localhost:8082/api'

// 结算表单数据
const checkoutForm = ref({
  orderType: 1, // 1: 堂食, 2: 外卖
  tableNumber: '',
  address: '',
  phone: userStore.userInfo?.phone || '',
  remark: ''
})

// 最终总价（包含配送费）
const finalTotal = computed(() => {
  const total = cartStore.total
  return checkoutForm.value.orderType === 2 ? total + deliveryFee : total
})

// 表单验证规则
const checkoutRules = {
  orderType: [
    { required: true, message: '请选择订单类型', trigger: 'change' }
  ],
  tableNumber: [
    { required: true, message: '请选择桌号', trigger: 'change' }
  ],
  address: [
    { required: true, message: '请输入送餐地址', trigger: 'blur' },
    { min: 5, max: 100, message: '地址长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 获取可用桌号
const fetchAvailableTables = async () => {
  try {
    const response = await orderApi.getOccupiedTables()
    occupiedTables.value = response.data || []
  } catch (error) {
    console.error('获取桌号信息失败:', error)
    ElMessage.warning('获取桌号信息失败，将使用默认设置')
    // 使用空数组作为默认值，表示所有桌号都可用
    occupiedTables.value = []
  }
}

// 格式化价格
const formatPrice = (price) => {
  return Number(price).toFixed(2)
}

// 修改数量
const handleQuantityChange = (itemId, quantity, item) => {
  if (quantity === 0) {
    // 当数量为0时，询问用户是否要删除
    ElMessageBox.confirm(
      `将${item.fdName}的数量设为0将从购物车中移除，是否继续？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      cartStore.removeItem(itemId)
    }).catch(() => {
      // 如果用户取消，将数量设回1
      item.quantity = 1
      cartStore.updateItemQuantity(itemId, 1)
    })
  } else {
    cartStore.updateItemQuantity(itemId, quantity)
  }
}

// 删除商品
const handleRemoveItem = (item) => {
  ElMessageBox.confirm(
    `确定要从购物车中删除 ${item.fdName} 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    cartStore.removeItem(item.id)
  }).catch(() => {})
}

// 清空购物车
const handleClearCart = () => {
  ElMessageBox.confirm(
    '确定要清空购物车吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    cartStore.clearCart()
  }).catch(() => {})
}

// 处理结算按钮点击
const handleCheckout = async () => {
  // 重置表单
  checkoutForm.value = {
    orderType: 1,
    tableNumber: '',
    address: userStore.userInfo?.address || '',
    phone: userStore.userInfo?.phone || '',
    remark: ''
  }
  
  // 获取可用桌号
  await fetchAvailableTables()
  
  // 显示结算对话框
  checkoutDialogVisible.value = true
}

// 提交订单
const submitOrder = async () => {
  if (!checkoutFormRef.value) return
  
  await checkoutFormRef.value.validate(async (valid) => {
    if (valid) {
      // 检查余额是否足够
      const totalAmount = finalTotal.value
      if (userStore.userInfo.balance < totalAmount) {
        ElMessage.error(`余额不足，还差 ¥${(totalAmount - userStore.userInfo.balance).toFixed(2)}`)
        return
      }

      loading.value = true
      try {
        // 准备订单数据
        const orderData = {
          userId: userStore.userInfo.id,
          sum: Math.round(totalAmount * 100), // 转换为整数，单位为分
          orderType: checkoutForm.value.orderType,
          tableNumber: checkoutForm.value.orderType === 1 ? checkoutForm.value.tableNumber : null,
          address: checkoutForm.value.orderType === 2 ? checkoutForm.value.address : null,
          phone: checkoutForm.value.phone,
          remark: checkoutForm.value.remark || '',
          foodLists: cartStore.items.map(item => ({
            fdId: item.id,
            orId: null, // 由后端填充
            fdName: item.fdName,
            price: Math.round(item.price * 100), // 转换为整数，单位为分
            count: Number(item.quantity),
            sum: Math.round(item.price * item.quantity * 100) // 转换为整数，单位为分
          }))
        }

        // 创建订单
        await orderApi.createOrder(orderData)
        ElMessage.success('下单成功')
        
        // 清空购物车
        cartStore.clearCart()
        
        // 关闭对话框
        checkoutDialogVisible.value = false

        // 刷新用户信息（获取新的余额）
        await userStore.fetchUserInfo()
        
        // 跳转到订单页面
        router.push('/orders')
      } catch (error) {
        console.error('下单失败:', error)
        ElMessage.error(error.message || '下单失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(10px, 3vw, 20px);
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
  font-size: clamp(20px, 4vw, 24px);
}

.cart-content {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.food-info {
  display: flex;
  align-items: center;
}

.food-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  margin-right: 15px;
}

.food-details {
  flex: 1;
}

.food-details h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.checkout-section {
  margin-top: 20px;
  padding: 20px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
}

.total-info {
  font-size: 16px;
}

.total-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
  margin-left: 10px;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .food-image {
    width: 60px;
    height: 60px;
  }

  .food-details h3 {
    font-size: 14px;
  }

  .checkout-section {
    flex-direction: column;
    gap: 10px;
  }

  .total-info {
    text-align: center;
    width: 100%;
  }

  :deep(.el-button--large) {
    width: 100%;
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .cart-content {
    background-color: #2b2b2b;
  }

  .food-details h3 {
    color: #e0e0e0;
  }

  .checkout-section {
    border-top-color: #4c4c4c;
  }
}

.checkout-info {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-size: 14px;
}

.info-item.total {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px dashed #ebeef5;
  font-size: 16px;
  font-weight: bold;
}
</style> 