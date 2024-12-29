import apiService from './apiService';
import API_ENDPOINTS from '../utils/api';

interface LoginResponse {
    accessToken: string;
    refreshToken: string;
    role: string;
}

interface RegisterData {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
}

interface AddUserData {
    firstName: string;
    lastName: string;
    email: string;
    role: string;
}

interface VerifyCodeData {
    code: string;
    email?: string;
    password?: string;
}

const authService = {
    /**
     * Login Function: Authenticates the user and stores tokens and role in localStorage
     */
    login: async (email: string, password: string): Promise<LoginResponse | null> => {
        try {
            const response = await apiService.post<LoginResponse>(API_ENDPOINTS.LOGIN, { email, password });

            if (response.data) {
                // Save tokens and role to local storage
                localStorage.setItem('accessToken', response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);
                localStorage.setItem('role', response.data.role);
            }

            return response.data;
        } catch (error: any) {
            console.error('Login failed', error.response?.data || error.message);
            throw new Error(error.response?.data || 'Login failed');
        }
    },

    /**
     * Register Function: Registers an organization or user
     */
    register: async (registrationData: RegisterData): Promise<unknown> => {
        try {
            const response = await apiService.post(API_ENDPOINTS.REGISTER, registrationData);
            return response.data;
        } catch (error: any) {
            console.error('Registration failed', error.response?.data || error.message);
            throw new Error(error.response?.data || 'Registration failed');
        }
    },

    /**
     * Verify Code Function: Verifies a unique code and sets email/password for users
     */
    verifyCode: async (data: VerifyCodeData): Promise<unknown> => {
        try {
            const response = await apiService.post(API_ENDPOINTS.VERIFY_CODE, data);
            return response.data;
        } catch (error: any) {
            console.error('Code verification failed', error.response?.data || error.message);
            throw new Error(error.response?.data || 'Code verification failed');
        }
    },

    /**
     * Add User Function: Adds a new teacher or student by the super-admin
     */
    addUser: async (userData: AddUserData): Promise<unknown> => {
        try {
            const response = await apiService.post(API_ENDPOINTS.ADD_USER, userData);
            return response.data;
        } catch (error: any) {
            console.error('Adding user failed', error.response?.data || error.message);
            throw new Error(error.response?.data || 'Adding user failed');
        }
    },
    getUserInfo: async (): Promise<unknown> => {
        return apiService.get(API_ENDPOINTS.USER_INFO);
    },
    /**
     * Logout Function: Clears tokens and role from localStorage
     */
    logout: (): void => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('role');
    },
};

export default authService;
