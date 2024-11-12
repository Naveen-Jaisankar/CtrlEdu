import axios from 'axios';
import { refreshAccessToken, logout } from './authService';

const api = axios.create({
    baseURL: 'http://localhost:8081',
});

// Request interceptor to add the access token to the headers
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => Promise.reject(error));

// Response interceptor to handle 401 errors and refresh the token
api.interceptors.response.use((response) => response, async (error) => {
    const originalRequest = error.config;
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;
        try {
            const newAccessToken = await refreshAccessToken();
            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
            return api(originalRequest);
        } catch (refreshError) {
            // If refreshing fails, log out the user
            logout();
            return Promise.reject(refreshError);
        }
    }
    return Promise.reject(error);
});

export default api;
