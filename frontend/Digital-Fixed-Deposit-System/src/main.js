// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import API from './config/api.js'
import logger from './utils/logger.js'

const app = createApp(App)

app.use(router)
app.config.globalProperties.$api = API
app.config.globalProperties.$logger = logger

app.mount('#app')

logger.info('Digital Fixed Deposit System mounted successfully', {
  version: import.meta.env.VITE_APP_VERSION || '1.0.0',
  environment: import.meta.env.MODE,
  logLevel: import.meta.env.VITE_LOG_LEVEL || 'INFO'
})
