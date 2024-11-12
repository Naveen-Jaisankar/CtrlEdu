const API_BASE_URL = 'http://localhost:8080/api'; // Base URL for API requests

// Function to get the stored token from localStorage
const getToken = () => localStorage.getItem('accessToken');

// Wrapper function to handle API requests with Authorization header
const apiFetch = async (url: string, options: RequestInit = {}) => {
    const token = getToken();
    const headers = {
        ...options.headers,
        'Authorization': `Bearer ${token}`,
    };

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers,
    });

    if (response.status === 401) {
        // Handle unauthorized access (e.g., redirect to login page)
        window.location.href = '/login';
    }

    return response.json();
};

export default apiFetch;
