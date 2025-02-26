/**
 * Restaurant Ordering System - Admin System Settings
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="page-container settings-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>系统设置</h2>
        </div>
      </el-col>
    </el-row>

    <el-card class="settings-card">
      <el-form
        ref="formRef"
        :model="settingsForm"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="餐厅桌位数量" prop="tableCount">
          <el-input-number
            v-model="settingsForm.tableCount"
            :min="1"
            :max="100"
            style="width: 200px"
          />
          <div class="form-item-tip">设置餐厅可用的桌位总数</div>
        </el-form-item>

        <el-form-item label="外卖配送费" prop="deliveryFee">
          <el-input-number
            v-model="settingsForm.deliveryFee"
            :min="0"
            :precision="2"
            :step="0.5"
            style="width: 200px"
          />
          <div class="form-item-tip">设置外卖订单的配送费用</div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            保存设置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { settingApi } from '../../api'

const loading = ref(false)
const formRef = ref(null)

// 设置表单数据
const settingsForm = ref({
  tableCount: 20,
  deliveryFee: 5
})

// 表单验证规则
const formRules = {
  tableCount: [
    { required: true, message: '请输入桌位数量', trigger: 'blur' },
    { type: 'number', min: 1, max: 100, message: '桌位数量必须在1-100之间', trigger: 'blur' }
  ],
  deliveryFee: [
    { required: true, message: '请输入配送费', trigger: 'blur' },
    { type: 'number', min: 0, message: '配送费不能小于0', trigger: 'blur' }
  ]
}

// 获取设置
const fetchSettings = async () => {
  loading.value = true
  try {
    const response = await settingApi.getSettings()
    const settings = response.data
    settingsForm.value = {
      tableCount: Number(settings.find(s => s.setting_key === 'table_count')?.setting_value || 20),
      deliveryFee: Number(settings.find(s => s.setting_key === 'delivery_fee')?.setting_value || 5)
    }
  } catch (error) {
    console.error('获取设置失败:', error)
    ElMessage.error('获取设置失败')
  } finally {
    loading.value = false
  }
}

// 提交设置
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 更新桌号数量
        await settingApi.updateTableCount(settingsForm.value.tableCount)
        // 更新配送费
        await settingApi.updateDeliveryFee(settingsForm.value.deliveryFee)
        
        ElMessage.success('设置已更新')
      } catch (error) {
        console.error('更新设置失败:', error)
        ElMessage.error('更新设置失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 组件挂载时获取设置
onMounted(() => {
  fetchSettings()
})
</script>

<style scoped>
.settings-container {
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

.settings-card {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  padding: 20px;
}

.form-item-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .settings-card {
    background-color: #2b2b2b;
  }

  .form-item-tip {
    color: #a0a0a0;
  }
}
</style> 