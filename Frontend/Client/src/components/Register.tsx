import React, { useState } from 'react';
import axios from 'axios';

const Register: React.FC = () => {
    const [role, setRole] = useState<string>('');
    const [name, setName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [invitationCode, setInvitationCode] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        if (password !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        const requestData = {
            role,
            name,
            email,
            invitationCode: role === 'student' || role === 'teacher' ? invitationCode : null,
            password,
        };

        try {
            const response = await axios.post(
                'http://localhost:8081/api/auth/register', 
                requestData, 
                { withCredentials: true }
            );
            setSuccess("Registration successful!");
            setRole('');
            setName('');
            setEmail('');
            setInvitationCode('');
            setPassword('');
            setConfirmPassword('');
        } catch (err) {
            setError("Registration failed. Please check your details or try again.");
        }
    };

    return (
        <div>
            <h2>Register</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Register as:</label>
                    <select value={role} onChange={(e) => setRole(e.target.value)} required>
                        <option value="">Select role</option>
                        <option value="super-admin">Organization (Super Admin)</option>
                        <option value="student">Student</option>
                        <option value="teacher">Teacher</option>
                    </select>
                </div>

                {role && (
                    <>
                        <div>
                            <label>Name:</label>
                            <input
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                required
                            />
                        </div>
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
                        <div>
                            <label>Confirm Password:</label>
                            <input
                                type="password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>

                        {(role === 'student' || role === 'teacher') && (
                            <div>
                                <label>Invitation Code:</label>
                                <input
                                    type="text"
                                    value={invitationCode}
                                    onChange={(e) => setInvitationCode(e.target.value)}
                                    required
                                />
                            </div>
                        )}

                        <button type="submit">Register</button>
                    </>
                )}
            </form>

            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
        </div>
    );
};

export default Register;
