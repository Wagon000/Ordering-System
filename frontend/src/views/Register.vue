/**
 * Restaurant Ordering System - Register View
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="page-container register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>用户注册</h2>
        </div>
      </template>
      
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-position="top"
        :size="screenSize"
      >
        <el-form-item label="用户名" prop="usName">
          <el-input v-model="registerForm.usName" placeholder="请输入用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="psd">
          <el-input
            v-model="registerForm.psd"
            type="password"
            placeholder="请输入密码"
            show-password
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPsd">
          <el-input
            v-model="registerForm.confirmPsd"
            type="password"
            placeholder="请再次输入密码"
            show-password
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="手机号码（选填）" prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入手机号码"
          >
            <template #prefix>
              <el-icon><Phone /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="送餐地址（选填）" prop="address">
          <el-input
            v-model="registerForm.address"
            type="textarea"
            :rows="2"
            placeholder="请输入送餐地址"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" class="submit-btn">
            注册
          </el-button>
        </el-form-item>
        
        <div class="login-link">
          已有账号? <router-link to="/login">立即登录</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Phone } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'

const router = useRouter()
const loading = ref(false)
const registerFormRef = ref(null)
const userStore = useUserStore()
const windowWidth = ref(window.innerWidth)

// 监听窗口大小变化
const handleResize = () => {
  windowWidth.value = window.innerWidth
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

// 根据屏幕宽度计算组件大小
const screenSize = computed(() => {
  if (windowWidth.value < 768) {
    return 'large'
  }
  return 'default'
})

// 注册表单数据
const registerForm = reactive({
  usName: '',
  psd: '',
  confirmPsd: '',
  phone: '',
  address: ''
})

// 注册表单验证规则
const registerRules = {
  usName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  psd: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPsd: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.psd) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ]
}

// 注册处理
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true
        const { confirmPsd, ...registerData } = registerForm
        // 如果手机号码和地址为空，则不传递这些字段
        if (!registerData.phone) delete registerData.phone
        if (!registerData.address) delete registerData.address
        
        // 确保只能注册普通用户
        registerData.role = 0
        
        await userStore.register(registerData)
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (error) {
        console.error('注册错误:', error)
        ElMessage.error(error.response?.data?.message || '注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: clamp(10px, 3vw, 20px);
}

.register-card {
  width: 100%;
  max-width: clamp(300px, 90vw, 400px);
  margin: auto;
  transition: all 0.3s ease;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .register-card {
    margin: 0;
  }

  .el-form-item {
    margin-bottom: 15px;
  }

  .el-input {
    font-size: 16px;
  }

  .submit-btn {
    height: 44px;
    font-size: 16px;
  }
}

.card-header {
  text-align: center;
  padding: 20px 0;
}

.card-header h2 {
  margin: 0;
  color: #409EFF;
  font-size: clamp(20px, 4vw, 24px);
}

.submit-btn {
  width: 100%;
  margin-top: 10px;
}

.login-link {
  text-align: center;
  margin-top: 15px;
  color: #606266;
  font-size: clamp(14px, 3vw, 16px);
}

.login-link a {
  color: #409EFF;
  text-decoration: none;
  transition: color 0.3s ease;
}

.login-link a:hover {
  text-decoration: underline;
  color: #66b1ff;
}

/* 表单项样式 */
:deep(.el-form-item__label) {
  font-size: clamp(14px, 3vw, 16px);
  color: #606266;
}

:deep(.el-input__inner) {
  transition: all 0.3s ease;
}

:deep(.el-input__inner:focus) {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .register-container {
    background-color: #1a1a1a;
  }

  .register-card {
    background-color: #2b2b2b;
  }

  .card-header h2 {
    color: #409EFF;
  }

  :deep(.el-form-item__label) {
    color: #e0e0e0;
  }

  :deep(.el-input__inner) {
    background-color: #363636;
    color: #e0e0e0;
    border-color: #4c4c4c;
  }

  .login-link {
    color: #a0a0a0;
  }
}

/* 触摸设备优化 */
@media (hover: none) {
  .submit-btn {
    cursor: default;
    -webkit-tap-highlight-color: transparent;
  }

  :deep(.el-input__inner) {
    -webkit-tap-highlight-color: transparent;
  }
}
</style> 