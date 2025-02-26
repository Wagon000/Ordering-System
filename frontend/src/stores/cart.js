/**
 * Restaurant Ordering System - Shopping Cart Store
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

export const useCartStore = defineStore('cart', () => {
  // 状态
  const items = ref(JSON.parse(localStorage.getItem('cart') || '[]'))
  
  // 计算属性
  const count = computed(() => items.value.length)
  const isEmpty = computed(() => count.value === 0)
  const total = computed(() => {
    return items.value.reduce((sum, item) => {
      // 确保price和quantity都是数字类型
      const price = Number(item.price)
      const quantity = Number(item.quantity)
      return sum + (price * quantity)
    }, 0)
  })
  
  // 保存到本地存储
  const saveToLocalStorage = () => {
    localStorage.setItem('cart', JSON.stringify(items.value))
  }
  
  // 动作
  const addItem = (food) => {
    // 检查商品是否已经在购物车中
    const existingItem = items.value.find(item => item.id === food.id)
    
    if (existingItem) {
      // 如果已存在，增加数量
      existingItem.quantity += 1
      ElMessage.success(`已将 ${food.fdName} 的数量增加到 ${existingItem.quantity}`)
    } else {
      // 如果不存在，添加到购物车
      items.value.push({
        id: food.id,
        fdName: food.fdName,
        price: Number(food.price), // 确保价格是数字类型
        photo: food.photo,
        quantity: 1
      })
      ElMessage.success(`已将 ${food.fdName} 添加到购物车`)
    }
    
    saveToLocalStorage()
  }
  
  const updateItemQuantity = (itemId, quantity) => {
    const item = items.value.find(item => item.id === itemId)
    
    if (item) {
      // 如果数量为0，则删除该商品
      if (quantity === 0) {
        removeItem(itemId)
        return
      }
      
      item.quantity = Number(quantity)
      saveToLocalStorage()
      ElMessage.success('购物车已更新')
    }
  }
  
  const removeItem = (itemId) => {
    const index = items.value.findIndex(item => item.id === itemId)
    
    if (index !== -1) {
      const removedItem = items.value[index]
      items.value.splice(index, 1)
      ElMessage.success(`已从购物车中移除 ${removedItem.fdName}`)
      saveToLocalStorage()
    }
  }
  
  const clearCart = () => {
    items.value = []
    ElMessage.success('购物车已清空')
    saveToLocalStorage()
  }
  
  return {
    items,
    count,
    isEmpty,
    total,
    addItem,
    updateItemQuantity,
    removeItem,
    clearCart
  }
}) 