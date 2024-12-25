import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

const Login: React.FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        try {
            // Call the backend login API
            const response = await axios.post('http://localhost:8081/api/auth/login', {
                email,
                password,
            });

            // Extract the access token, refresh token, and role
            const { accessToken, refreshToken, role } = response.data.data;
            console.log(response.data)
            console.log(response.data.data.role);    
            console.log(response.data.data.accessToken);    
            console.log(response.data.data.refreshToken);  
            // Store tokens and role in localStorage
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('role', role);

            // Navigate to the appropriate dashboard based on the role
            if (role === 'super-admin') {
                navigate('/admin-dashboard');
            } else if (role === 'teacher') {
                navigate('/teacher-dashboard');
            } else if (role === 'student') {
                navigate('/student-dashboard');
            } else {
                setError('Invalid role. Please contact support.');
            }
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                // Handle error from backend
                setError(error.response.data.message || 'Login failed. Please try again.');
            } else {
                setError('An unexpected error occurred. Please try again.');
            }
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center dark:bg-neutral-900 bg-white">
            <div className="bg-white dark:bg-neutral-800 shadow-lg rounded-lg w-96 p-6">
                <h2 className="text-2xl font-bold mb-6 dark:text-white text-neutral-900">Log In</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block text-sm font-medium mb-2 dark:text-gray-300 text-neutral-700">Email</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full px-4 py-2 border rounded-md dark:bg-neutral-700 dark:text-white bg-gray-50 text-neutral-900"
                            required
                        />
                    </div>
                    <div className="mb-6">
                        <label className="block text-sm font-medium mb-2 dark:text-gray-300 text-neutral-700">Password</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full px-4 py-2 border rounded-md dark:bg-neutral-700 dark:text-white bg-gray-50 text-neutral-900"
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-orange-500 hover:bg-orange-600 text-white py-2 rounded-md"
                    >
                        Login
                    </button>
                </form>
                {error && <p className="mt-4 text-sm text-red-500">{error}</p>}
                <p className="mt-6 text-center text-sm dark:text-gray-300 text-neutral-700">
                    Don't have an account?{' '}
                    <Link to="/register" className="text-orange-500 hover:underline">
                        Register
                    </Link>
                </p>
            </div>
        </div>
    );
};

export default Login;
