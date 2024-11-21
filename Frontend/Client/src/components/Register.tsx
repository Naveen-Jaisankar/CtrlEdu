import React, { useState } from 'react';
import axios from 'axios';

const Register: React.FC = () => {
    const [role, setRole] = useState<string>(''); // Role selected by the user
    const [organizationName, setOrganizationName] = useState<string>(''); // Organization name for super-admin
    const [firstName, setFirstName] = useState<string>('');
    const [lastName, setLastName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [uniqueCode, setUniqueCode] = useState<string>('');
    const [step, setStep] = useState<number>(0); // Step 0: Role Selection, Step 1: Enter Details

    const handleRoleSelection = (selectedRole: string) => {
        setRole(selectedRole);
        setStep(1); // Move to the next step to enter details
    };

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            if (role === 'organization') {
                // Organization registration
                await axios.post('http://localhost:8081/api/auth/register', {
                    organizationName,
                    firstName,
                    lastName,
                    email,
                    password,
                });
                alert('Organization registered successfully!');
            } else {
                // Student/Teacher registration
                if (step === 1) {
                    // Validate unique code
                    const response = await axios.post('http://localhost:8081/api/auth/verify-code', {
                        code: uniqueCode,
                    });
                    setStep(2); // Move to email/password setup
                    alert('Unique code validated. Please set your email and password.');
                } else if (step === 2) {
                    // Set email and password
                    await axios.post('http://localhost:8081/api/auth/verify-code', {
                        code: uniqueCode,
                        email,
                        password,
                    });
                    alert('Registration complete! You can now log in.');
                }
            }
        } catch (error: any) {
            alert(error.response?.data || 'Error during registration');
        }
    };

    return (
        <div>
            <h2>Register</h2>
            {step === 0 && (
                // Step 0: Role Selection
                <div>
                    <label>Select Role:</label>
                    <select value={role} onChange={(e) => handleRoleSelection(e.target.value)} required>
                        <option value="">-- Select Role --</option>
                        <option value="organization">Organization</option>
                        <option value="student">Student</option>
                        <option value="teacher">Teacher</option>
                    </select>
                </div>
            )}

            {step === 1 && role === 'organization' && (
                // Organization Registration
                <form onSubmit={handleRegister}>
                    <div>
                        <label>Organization Name:</label>
                        <input
                            type="text"
                            value={organizationName}
                            onChange={(e) => setOrganizationName(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>First Name:</label>
                        <input
                            type="text"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>Last Name:</label>
                        <input
                            type="text"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
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
                    <button type="submit">Register</button>
                </form>
            )}

            {step === 1 && (role === 'student' || role === 'teacher') && (
                // Student/Teacher: Step 1 - Validate Unique Code
                <form onSubmit={handleRegister}>
                    <div>
                        <label>Unique Code:</label>
                        <input
                            type="text"
                            value={uniqueCode}
                            onChange={(e) => setUniqueCode(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit">Validate Code</button>
                </form>
            )}

            {step === 2 && (role === 'student' || role === 'teacher') && (
                // Student/Teacher: Step 2 - Email and Password Setup
                <form onSubmit={handleRegister}>
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
                    <button type="submit">Complete Registration</button>
                </form>
            )}
        </div>
    );
};

export default Register;
