// src/main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import API, { API_CONFIG } from './config/api.js';
import logger from './utils/logger.js';

// ---------- Global Axios Configuration ----------
API.defaults.baseURL = import.meta.env.VITE_API_BASE_URL || API_CONFIG.BASE_URL;
API.defaults.timeout = API_CONFIG.TIMEOUT;

// Request interceptor
API.interceptors.request.use(
  config => {
    logger.debug('HTTP Request', {
      method: config.method?.toUpperCase(),
      url: config.url,
      params: config.params,
      data: config.data
    });
    return config;
  },
  error => {
    logger.error('HTTP Request Error', error);
    return Promise.reject(error);
  }
);

// Response interceptor
API.interceptors.response.use(
  response => {
    logger.debug('HTTP Response', {
      status: response.status,
      url: response.config?.url,
      data: response.data
    });
    return response; // return full response
  },
  error => {
    logger.logApiError(error.config?.url || 'Unknown endpoint', error, `axios-${Date.now()}`);
    return Promise.reject(error);
  }
);

// ---------- Initialize Vue Application ----------
const app = createApp(App);
app.use(router);

// Make axios (API instance) and logger available globally
app.config.globalProperties.$api = API;
app.config.globalProperties.$logger = logger;

app.mount('#app');

logger.info('Digital Fixed Deposit System mounted successfully', {
  version: import.meta.env.VITE_APP_VERSION || '1.0.0',
  environment: import.meta.env.MODE,
  logLevel: import.meta.env.VITE_LOG_LEVEL || 'INFO'
});





































// import { createApp } from 'vue'
// import './style.css'
// import App from './App.vue'
// import router from './router'
// import axios from 'axios'
// import API_CONFIG from './config/api.js'
// import logger from './utils/logger.js'

// // Configure axios defaults
// axios.defaults.timeout = API_CONFIG.TIMEOUT

// // Add axios interceptors for logging
// axios.interceptors.request.use(
//     config => {
//         logger.debug('HTTP Request', {
//             method: config.method?.toUpperCase(),
//             url: config.url,
//             timeout: config.timeout
//         });
//         return config;
//     },
//     error => {
//         logger.error('HTTP Request Error', error);
//         return Promise.reject(error);
//     }
// );

// axios.interceptors.response.use(
//     response => {
//         logger.debug('HTTP Response', {
//             status: response.status,
//             url: response.config?.url
//         });
//         return response;
//     },
//     error => {
//         logger.logApiError(
//             error.config?.url || 'Unknown endpoint',
//             error,
//             `axios-${Date.now()}`
//         );
//         return Promise.reject(error);
//     }
// );

// // Initialize application
// logger.info('Digital Fixed Deposit System starting', {
//     version: import.meta.env.VITE_APP_VERSION || '1.0.0',
//     environment: import.meta.env.MODE,
//     logLevel: import.meta.env.VITE_LOG_LEVEL || 'INFO'
// });

// // Make axios and logger available globally
// const app = createApp(App)
// app.use(router)
// app.config.globalProperties.$axios = axios
// app.config.globalProperties.$logger = logger

// app.mount('#app')

// logger.info('Application mounted successfully');
