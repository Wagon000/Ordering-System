/**
 * Restaurant Ordering System - User Profile View
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="page-container profile-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>个人信息</h2>
        </div>
      </el-col>
    </el-row>

    <!-- 账户信息卡片 -->
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>账户信息</span>
        </div>
      </template>

      <div class="account-info">
        <div class="info-item">
          <div class="info-label">账户余额</div>
          <div class="info-value balance">¥{{ formatPrice(userInfo.balance) }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">总消费金额</div>
          <div class="info-value consumption">¥{{ formatPrice(userInfo.totalConsumption) }}</div>
        </div>
      </div>
    </el-card>

    <!-- 基本信息卡片 -->
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
          <el-button
            type="primary"
            class="edit-button"
            @click="handleEditClick"
          >
            <div class="button-content">
              <el-icon v-if="editing"><Check /></el-icon>
              <el-icon v-else><Edit /></el-icon>
              <span style="margin-left: 5px;">{{ editing ? '保存' : '编辑' }}</span>
            </div>
          </el-button>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="userForm"
        :rules="formRules"
        label-width="100px"
        :disabled="!editing"
      >
        <el-form-item label="用户名" prop="usName">
          <el-input v-model="userForm.usName" disabled />
        </el-form-item>

        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>

        <el-form-item label="送餐地址" prop="address">
          <el-input
            v-model="userForm.address"
            type="textarea"
            :rows="2"
          />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 密码修改卡片 -->
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
        </div>
      </template>

      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            show-password
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleChangePassword">
            修改密码
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import { userApi } from '../api'
import { Check, Edit } from '@element-plus/icons-vue'

const userStore = useUserStore()
const formRef = ref(null)
const passwordFormRef = ref(null)
const editing = ref(false)

// 从store获取用户信息
const userInfo = computed(() => userStore.userInfo)

// 用户表单数据
const userForm = reactive({
  usName: '',
  phone: '',
  address: ''
})

// 监听用户信息变化，更新表单数据
watch(() => userStore.userInfo, (newUserInfo) => {
  if (newUserInfo) {
    userForm.usName = newUserInfo.usName || ''
    userForm.phone = newUserInfo.phone || ''
    userForm.address = newUserInfo.address || ''
    console.log('用户信息已更新到表单:', userForm)
  }
}, { immediate: true })

// 密码表单数据
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 格式化价格
const formatPrice = (price) => {
  if (price === null || price === undefined) return '0.00'
  // 确保price是数字类型
  return parseFloat(price).toFixed(2)
}

// 表单验证规则
const formRules = {
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 处理编辑点击
const handleEditClick = async () => {
  if (editing.value) {
    // 保存更改
    try {
      await formRef.value.validate()
      
      // 构建完整的用户信息对象，确保包含id字段
      const userData = {
        id: userInfo.value.id,
        usName: userForm.usName,
        phone: userForm.phone,
        address: userForm.address,
        // 保留其他字段不变
        balance: userInfo.value.balance,
        totalConsumption: userInfo.value.totalConsumption,
        role: userInfo.value.role
      }
      
      console.log('发送更新用户信息:', userData)
      await userApi.updateUserInfo(userData)
      ElMessage.success('保存成功')
      editing.value = false
      // 更新store中的用户信息
      await userStore.fetchUserInfo()
    } catch (error) {
      console.error('保存用户信息失败:', error)
      if (error.message) {
        ElMessage.error(error.message)
      } else {
        ElMessage.error('保存失败')
      }
    }
  } else {
    editing.value = true
  }
}

// 处理密码修改
const handleChangePassword = async () => {
  try {
    await passwordFormRef.value.validate()
    await userApi.changePassword(passwordForm)
    ElMessage.success('密码修改成功')
    // 清空表单
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
    // 重置表单验证
    passwordFormRef.value.resetFields()
  } catch (error) {
    console.error('修改密码失败:', error)
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('修改密码失败')
    }
  }
}

// 页面加载时刷新用户信息
onMounted(async () => {
  try {
    await userStore.fetchUserInfo()
    console.log('用户信息:', userStore.userInfo)
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
})
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: clamp(10px, 3vw, 20px);
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: clamp(20px, 4vw, 24px);
}

.profile-card {
  margin-bottom: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.profile-card:last-child {
  margin-bottom: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

.card-header span {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

/* 编辑按钮样式 */
.edit-button {
  padding: 8px 15px;
}

.button-content {
  display: flex;
  align-items: center;
  justify-content: center;
}

.account-info {
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.info-item {
  text-align: center;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.info-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.info-value {
  font-size: 24px;
  font-weight: bold;
}

.info-value.balance {
  color: #67c23a;
}

.info-value.consumption {
  color: #f56c6c;
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .profile-card {
    background-color: #2b2b2b;
  }

  .card-header {
    border-bottom-color: #4c4c4c;
  }

  .card-header span {
    color: #e0e0e0;
  }

  .info-item {
    background-color: #363636;
  }

  .info-label {
    color: #a0a0a0;
  }
}
</style> 