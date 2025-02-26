/**
 * Restaurant Ordering System - Login View
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>{{ activeTab === 'login' ? '用户登录' : '用户注册' }}</h2>
        </div>
      </template>
      
      <!-- 登录表单 -->
      <el-form
        v-if="activeTab === 'login'"
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-position="top"
      >
        <el-form-item label="用户名" prop="usName">
          <el-input v-model="loginForm.usName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="psd">
          <el-input v-model="loginForm.psd" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="activeTab = 'register'" style="width: 100%">
            没有账号?点击注册
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 注册表单 -->
      <el-form
        v-else
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-position="top"
      >
        <el-form-item label="用户名" prop="usName">
          <el-input v-model="registerForm.usName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="psd">
          <el-input v-model="registerForm.psd" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPsd">
          <el-input v-model="registerForm.confirmPsd" type="password" placeholder="请再次输入密码" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="registerForm.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width: 100%">
            注册
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="activeTab = 'login'" style="width: 100%">
            已有账号?返回登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('login')
const loading = ref(false)

// 登录表单
const loginFormRef = ref(null)
const loginForm = ref({
  usName: '',
  psd: ''
})

// 注册表单
const registerFormRef = ref(null)
const registerForm = ref({
  usName: '',
  psd: '',
  confirmPsd: '',
  phone: '',
  address: ''
})

// 表单验证规则
const loginRules = {
  usName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  psd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const registerRules = {
  usName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  psd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPsd: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.psd) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }]
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        console.log('登录请求详细信息:')
        console.log('用户名:', loginForm.value.usName)
        console.log('密码长度:', loginForm.value.psd.length)
        console.log('密码明文(仅用于调试):', loginForm.value.psd)
        
        const response = await userStore.login(loginForm.value)
        console.log('登录响应成功:', response)
        
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error) {
        console.error('登录失败详情:', error)
        
        // 显示更详细的错误信息
        if (error.response) {
          console.error('错误状态码:', error.response.status)
          console.error('错误数据:', error.response.data)
          
          // 显示服务器返回的具体错误信息
          const errorMsg = error.response.data?.message || error.message || '服务器错误'
          console.error('错误消息:', errorMsg)
          
          ElMessage.error(`登录失败: ${errorMsg}`)
        } else if (error.request) {
          console.error('请求已发送但没有收到响应')
          ElMessage.error('服务器无响应，请检查网络连接')
        } else {
          console.error('请求设置错误:', error.message)
          ElMessage.error(error.message || '登录失败，请检查网络连接')
        }
      } finally {
        loading.value = false
      }
    }
  })
}

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { confirmPsd, ...registerData } = registerForm.value
        await userStore.register(registerData)
        ElMessage.success('注册成功')
        activeTab.value = 'login'
        registerForm.value = {
          usName: '',
          psd: '',
          confirmPsd: '',
          phone: '',
          address: ''
        }
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error(error.message || '注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #409EFF;
}
</style> 