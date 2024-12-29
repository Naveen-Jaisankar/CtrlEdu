import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';

const apiService = {
    
    get: async (url: string, config: AxiosRequestConfig = {}): Promise<AxiosResponse> => {
        return axios.get(url, {
            ...config,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                ...config.headers,
            },
        });
    },

    post: async (url: string, data: unknown, config: AxiosRequestConfig = {}): Promise<AxiosResponse> => {
        return axios.post(url, data, {
            ...config,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                ...config.headers,
            },
        });
    },

    put: async (url: string, data: unknown, config: AxiosRequestConfig = {}): Promise<AxiosResponse> => {
        return axios.put(url, data, {
            ...config,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                ...config.headers,
            },
        });
    },

    delete: async (url: string, config: AxiosRequestConfig = {}): Promise<AxiosResponse> => {
        return axios.delete(url, {
            ...config,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                ...config.headers,
            },
        });
    },
};

export default apiService;
