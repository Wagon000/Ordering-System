/**
 * Restaurant Ordering System - Main Entry File
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createPinia } from 'pinia'
import router from './router'
import './style.css'
import App from './App.vue'

const app = createApp(App)

app.use(ElementPlus)
app.use(createPinia())
app.use(router)

app.mount('#app')
