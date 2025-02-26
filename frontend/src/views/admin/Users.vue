/**
 * Restaurant Ordering System - Admin Users Management
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="admin-users-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>用户管理</h2>
          <div class="header-controls">
            <!-- 搜索框 -->
            <el-input
              v-model="searchQuery"
              placeholder="搜索用户名、电话或地址..."
              prefix-icon="Search"
              clearable
              @input="handleSearch"
              style="width: 250px"
            />
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 用户列表 -->
    <el-table
      v-loading="loading"
      :data="filteredUserList"
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="usName" label="用户名" />
      <el-table-column prop="phone" label="手机号码" />
      <el-table-column prop="address" label="地址" show-overflow-tooltip />
      <el-table-column label="余额" width="120">
        <template #default="{ row }">
          <span class="price">¥{{ formatPrice(row.balance) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总消费" width="120">
        <template #default="{ row }">
          <span class="price">¥{{ formatPrice(row.totalConsumption) }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button
            type="primary"
            size="small"
            @click="handleEdit(row)"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            size="small"
            @click="handleDelete(row)"
          >
            删除
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

    <!-- 编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑用户"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="userForm"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="usName">
          <el-input v-model="userForm.usName" disabled />
        </el-form-item>

        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>

        <el-form-item label="地址" prop="address">
          <el-input
            v-model="userForm.address"
            type="textarea"
            :rows="2"
          />
        </el-form-item>

        <el-form-item label="余额" prop="balance">
          <el-input-number
            v-model="userForm.balance"
            :precision="2"
            :step="100"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { userApi } from '../../api'

// 状态
const loading = ref(false)
const dialogVisible = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const userList = ref([])
const formRef = ref(null)

// 表单数据
const userForm = reactive({
  id: null,
  usName: '',
  phone: '',
  address: '',
  balance: 0
})

// 表单验证规则
const formRules = {
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  balance: [
    { required: true, message: '请输入余额', trigger: 'blur' },
    { type: 'number', min: 0, message: '余额必须大于等于0', trigger: 'blur' }
  ]
}

// 搜索和筛选状态
const searchQuery = ref('')

// 过滤后的用户列表
const filteredUserList = computed(() => {
  let result = userList.value.filter(user => user.role !== -1) // 过滤掉管理员账号

  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(user => 
      user.usName.toLowerCase().includes(query) ||
      (user.phone && user.phone.includes(query)) ||
      (user.address && user.address.toLowerCase().includes(query))
    )
  }

  return result
})

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true
  try {
    const response = await userApi.getUserList(currentPage.value, pageSize.value)
    userList.value = response.data.rows
    total.value = response.data.totalCount
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 处理编辑
const handleEdit = (row) => {
  dialogVisible.value = true
  Object.assign(userForm, row)
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除用户"${row.usName}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await userApi.deleteUser(row.id)
      ElMessage.success('删除成功')
      fetchUserList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await userApi.updateUser(userForm)
        ElMessage.success('更新成功')
        dialogVisible.value = false
        fetchUserList()
      } catch (error) {
        console.error('更新失败:', error)
        ElMessage.error('更新失败')
      }
    }
  })
}

// 处理页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchUserList()
}

// 处理每页数量改变
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchUserList()
}

// 处理搜索
const handleSearch = debounce(() => {
  currentPage.value = 1 // 重置到第一页
  fetchUserList()
}, 300)

// 格式化价格
const formatPrice = (price) => {
  if (price === null || price === undefined) return '0.00'
  return Number(price).toFixed(2)
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
  fetchUserList()
})
</script>

<style scoped>
.admin-users-container {
  padding: 20px;
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
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 