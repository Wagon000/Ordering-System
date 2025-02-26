/**
 * Restaurant Ordering System - Admin Food Management
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
<template>
  <div class="admin-food-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h2>菜品管理</h2>
          <div class="header-controls">
            <!-- 搜索框 -->
            <el-input
              v-model="searchQuery"
              placeholder="搜索菜品名称..."
              prefix-icon="Search"
              clearable
              @input="handleSearch"
              style="width: 200px; margin-right: 15px;"
            />
            
            <!-- 价格排序 -->
            <el-select
              v-model="sortOrder"
              placeholder="价格排序"
              style="width: 120px; margin-right: 15px;"
              @change="handleSort"
            >
              <el-option label="默认排序" value="" />
              <el-option label="价格升序" value="asc" />
              <el-option label="价格降序" value="desc" />
            </el-select>
            
            <el-button type="primary" @click="handleAdd">
              添加菜品
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 菜品列表 -->
    <el-table
      v-loading="loading"
      :data="filteredAndSortedFoodList"
      style="width: 100%"
    >
      <el-table-column label="图片" width="120">
        <template #default="{ row }">
          <el-image
            :src="row.photo ? `${apiBaseUrl}${row.photo}` : '/placeholder.png'"
            style="width: 80px; height: 80px"
            fit="cover"
          />
        </template>
      </el-table-column>

      <el-table-column prop="fdName" label="名称" />
      <el-table-column prop="intro" label="介绍" show-overflow-tooltip />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }">
          ¥{{ formatPrice(row.price) }}
        </template>
      </el-table-column>

      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-switch
            v-model="row.fdStatus"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(row)"
          />
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

    <!-- 添加/编辑菜品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加菜品' : '编辑菜品'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="foodForm"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="名称" prop="fdName">
          <el-input v-model="foodForm.fdName" />
        </el-form-item>

        <el-form-item label="介绍" prop="intro">
          <el-input
            v-model="foodForm.intro"
            type="textarea"
            :rows="3"
          />
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input-number
            v-model="foodForm.price"
            :min="0"
            :precision="2"
            :step="0.1"
          />
        </el-form-item>

        <el-form-item label="图片" prop="photo">
          <el-upload
            class="food-uploader"
            :action="`${apiBaseUrl}/food/upload`"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-progress="handleUploadProgress"
            :before-upload="beforeUpload"
            accept="image/*"
          >
            <el-image
              v-if="foodForm.photo"
              :src="`${apiBaseUrl}${foodForm.photo}`"
              class="food-image"
              fit="cover"
              :preview-src-list="[`${apiBaseUrl}${foodForm.photo}`]"
            />
            <div v-else-if="uploadProgress" class="upload-progress">
              <el-progress type="circle" :percentage="uploadProgress" />
            </div>
            <el-icon v-else class="food-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">
            支持 jpg、png、gif 格式，大小不超过 2MB
          </div>
        </el-form-item>

        <el-form-item label="状态" prop="fdStatus">
          <el-switch
            v-model="foodForm.fdStatus"
            :active-value="1"
            :inactive-value="0"
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { foodApi } from '../../api'

// API基础URL
const apiBaseUrl = 'http://localhost:8082/api'

// 添加占位图片URL
const placeholderImage = '/src/assets/placeholder.png'

// 状态
const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('add')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const foodList = ref([])
const formRef = ref(null)
const searchQuery = ref('')
const sortOrder = ref('')
const uploadProgress = ref(0)

// 表单数据
const foodForm = reactive({
  id: null,
  fdName: '',
  intro: '',
  price: 0,
  photo: '',
  fdStatus: 1
})

// 表单验证规则
const formRules = {
  fdName: [
    { required: true, message: '请输入菜品名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  intro: [
    { required: true, message: '请输入菜品介绍', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格必须大于0', trigger: 'blur' }
  ]
}

// 上传头部信息
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token')}`
}))

// 过滤和排序后的菜品列表
const filteredAndSortedFoodList = computed(() => {
  let result = [...foodList.value]
  
  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(food => 
      food.fdName.toLowerCase().includes(query) ||
      food.intro.toLowerCase().includes(query)
    )
  }
  
  // 价格排序
  if (sortOrder.value) {
    result.sort((a, b) => {
      if (sortOrder.value === 'asc') {
        return a.price - b.price
      } else {
        return b.price - a.price
      }
    })
  }

  // 更新总数
  total.value = result.length
  
  // 分页处理
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return result.slice(start, end)
})

// 获取菜品列表
const fetchFoodList = async () => {
  loading.value = true
  try {
    const response = await foodApi.getFoodList()
    foodList.value = response.data
  } catch (error) {
    console.error('获取菜品列表失败:', error)
    ElMessage.error('获取菜品列表失败')
  } finally {
    loading.value = false
  }
}

// 处理添加
const handleAdd = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  foodForm.id = null
  foodForm.fdName = ''
  foodForm.intro = ''
  foodForm.price = 0
  foodForm.photo = ''
  foodForm.fdStatus = 1
}

// 处理编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  // 将价格从分转换为元
  const editData = {
    ...row,
    price: Number((row.price / 100).toFixed(2))
  }
  Object.assign(foodForm, editData)
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除菜品"${row.fdName}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await foodApi.deleteFood(row.id)
      ElMessage.success('删除成功')
      fetchFoodList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 处理状态改变
const handleStatusChange = async (row) => {
  try {
    await foodApi.updateFoodStatus(row.id, row.fdStatus)
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('状态更新失败:', error)
    ElMessage.error('状态更新失败')
    row.fdStatus = row.fdStatus === 1 ? 0 : 1
  }
}

// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 将价格从元转换为分
        const submitData = {
          ...foodForm,
          price: Math.round(foodForm.price * 100)
        }
        
        if (dialogType.value === 'add') {
          await foodApi.addFood(submitData)
          ElMessage.success('添加成功')
        } else {
          await foodApi.updateFood(submitData)
          ElMessage.success('更新成功')
        }
        
        dialogVisible.value = false
        fetchFoodList()
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
      }
    }
  })
}

// 处理上传进度
const handleUploadProgress = (event) => {
  uploadProgress.value = Math.round((event.loaded / event.total) * 100)
}

// 处理上传错误
const handleUploadError = (error) => {
  uploadProgress.value = 0
  ElMessage.error('图片上传失败，请重试')
  console.error('上传失败:', error)
}

// 处理上传成功
const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    foodForm.photo = response.data
    uploadProgress.value = 0
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.msg || '图片上传失败')
  }
}

// 上传前检查
const beforeUpload = (file) => {
  // 检查文件类型
  const isImage = /^image\/(jpeg|png|gif)$/.test(file.type)
  if (!isImage) {
    ElMessage.error('只能上传 jpg、png、gif 格式的图片!')
    return false
  }
  
  // 检查文件大小（2MB）
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  
  uploadProgress.value = 0
  return true
}

// 处理页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  // 不需要重新获取数据，计算属性会自动处理分页
}

// 处理每页数量改变
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
  // 不需要重新获取数据，计算属性会自动处理分页
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1 // 重置到第一页
  // 搜索逻辑已通过计算属性实现
}

// 处理排序
const handleSort = () => {
  currentPage.value = 1 // 重置到第一页
  // 排序逻辑已通过计算属性实现
}

// 在script部分添加formatPrice函数
const formatPrice = (price) => {
  return (price / 100).toFixed(2)
}

// 组件挂载时获取数据
onMounted(() => {
  fetchFoodList()
})
</script>

<style scoped>
.admin-food-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-controls {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.food-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.food-uploader:hover {
  border-color: var(--el-color-primary);
}

.food-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.food-image {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.upload-progress {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .food-uploader {
    border-color: #4c4c4c;
    background-color: #2b2b2b;
  }
  
  .food-uploader:hover {
    border-color: var(--el-color-primary);
  }
  
  .upload-progress {
    background: rgba(0, 0, 0, 0.7);
  }
  
  .upload-tip {
    color: #909399;
  }
}
</style> 