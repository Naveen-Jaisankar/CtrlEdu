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
            const { accessToken, refreshToken, role } = response.data;
            
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
        <div>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Login</button>
            </form>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <p>
                Don't have an account? <Link to="/register">Register</Link>
            </p>
        </div>
    );
};

export default Login;
