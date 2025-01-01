const API_BASE_URL = 'http://localhost:8081/api';

export const API_ENDPOINTS = {
    LOGIN: `${API_BASE_URL}/auth/login`,
    REGISTER: `${API_BASE_URL}/auth/register`,
    VERIFY_CODE: `${API_BASE_URL}/auth/verify-code`,
    ADD_USER: `${API_BASE_URL}/admin/add-user`,
    USER_INFO: '/api/auth/users',
};

export default API_ENDPOINTS;
