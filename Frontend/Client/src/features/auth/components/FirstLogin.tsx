import React, { useState } from 'react';
import authService from '../../../services/authService';

const FirstLogin: React.FC = () => {
    const [step, setStep] = useState(1); // 1: Code Verification, 2: Set Email and Password
    const [code, setCode] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleCodeSubmit = async () => {
        try {
            // Verify the code
            await authService.verifyCode({ code });
            setStep(2); // Move to the next step
        } catch (err: any) {
            setError(err.message || 'Invalid code');
        }
    };

    const handlePasswordSubmit = async () => {
        try {
            // Set email and password
            await authService.verifyCode({ code, email, password });
            alert('Password set successfully. You can now log in.');
            window.location.href = '/'; // Redirect to login page
        } catch (err: any) {
            setError(err.message || 'Failed to set password');
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <div className="bg-white p-8 rounded shadow-md w-96">
                {step === 1 ? (
                    <>
                        <h2 className="text-xl font-bold mb-4">Verify Your Code</h2>
                        <input
                            type="text"
                            placeholder="Enter unique code"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            className="border border-gray-300 rounded w-full p-2 mb-4"
                        />
                        <button
                            onClick={handleCodeSubmit}
                            className="bg-blue-500 text-white px-4 py-2 rounded w-full"
                        >
                            Verify Code
                        </button>
                    </>
                ) : (
                    <>
                        <h2 className="text-xl font-bold mb-4">Set Your Password</h2>
                        <input
                            type="email"
                            placeholder="Enter email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="border border-gray-300 rounded w-full p-2 mb-4"
                        />
                        <input
                            type="password"
                            placeholder="Enter password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="border border-gray-300 rounded w-full p-2 mb-4"
                        />
                        <button
                            onClick={handlePasswordSubmit}
                            className="bg-blue-500 text-white px-4 py-2 rounded w-full"
                        >
                            Set Password
                        </button>
                    </>
                )}
                {error && <p className="text-red-500 mt-2">{error}</p>}
            </div>
        </div>
    );
};

export default FirstLogin;
