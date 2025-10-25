



import axios from 'axios';
import logger from '../utils/logger.js';

// API configuration
const API_CONFIG = {
    BASE_URL: import.meta.env.VITE_API_URL || 'http://localhost:8084/api',

    //BASE_URL: import.meta.env.VITE_API_URL || 'http://localhost:8084',
    TIMEOUT: 10000 // 10 seconds
};

// Create axios instance
const API = axios.create({
    baseURL: API_CONFIG.BASE_URL,
    timeout: API_CONFIG.TIMEOUT
});

// Request interceptor
API.interceptors.request.use(
    config => {
        logger.debug('API Request', {
            method: config.method?.toUpperCase(),
            url: config.url,
            params: config.params,
            data: config.data
        });
        return config;
    },
    error => {
        logger.error('API Request Error', error);
        return Promise.reject(error);
    }
);

// Response interceptor
API.interceptors.response.use(
    response => {
        logger.debug('API Response', {
            status: response.status,
            url: response.config?.url,
            data: response.data
        });
        return response.data; // return only data for cleaner usage
    },
    error => {
        logger.logApiError(error.config?.url || 'Unknown endpoint', error);
        return Promise.reject(error);
    }
);

// API calls
export const bookFd = (payload) => API.post('/fd/book', payload);
export const getFds = (userId) => API.get(`/fd/user/${userId}`);

// Export config & instance if needed elsewhere
export { API_CONFIG, API };

export default API;











// // API Configuration
// const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8084/api'

// const API_CONFIG = {
//     BASE_URL,
//     ENDPOINTS: {
//         // FD related endpoints
//         FD_INTEREST: (fdId) => `${BASE_URL}/fd/${fdId}/interest`,
//         USER_FDS: (userId) => `${BASE_URL}/fd/user/${userId}`,

//         // Support ticket endpoints
//         CREATE_TICKET: () => `${BASE_URL}/support/new`,
//         USER_TICKETS: (userId) => `${BASE_URL}/support/user/${userId}`,
//         GET_TICKET: (ticketId) => `${BASE_URL}/support/${ticketId}`,
//         ALL_TICKETS: () => `${BASE_URL}/support/all`,
//     },
//     TIMEOUT: 10000,

//     // Helper method to get full endpoint URL
//     getEndpoint: (endpointKey, ...params) => {
//         const endpoint = API_CONFIG.ENDPOINTS[endpointKey]
//         return typeof endpoint === 'function' ? endpoint(...params) : endpoint
//     }
// }

// export default API_CONFIG