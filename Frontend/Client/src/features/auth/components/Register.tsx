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
                    organization_name: organizationName,
                    first_name: firstName,
                    last_name: lastName,
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
        <div className="min-h-screen flex flex-col items-center justify-center bg-neutral-900 text-white">
            <div className="bg-neutral-800 p-8 rounded-lg shadow-md w-full max-w-md">
                <h2 className="text-2xl font-bold text-center mb-6">
                    {step === 0
                        ? 'Sign up to CtrlEdu'
                        : role === 'organization'
                        ? 'Organization Registration'
                        : 'Complete Your Registration'}
                </h2>

                {step === 0 && (
                    // Step 0: Role Selection
                    <div className="grid grid-cols-1 gap-4 text-center">
                        <button
                            className="p-4 bg-orange-500 hover:bg-orange-600 rounded-lg text-white transition"
                            onClick={() => handleRoleSelection('organization')}
                        >
                            <div className="font-bold">School Owner/Administrator</div>
                        </button>
                        <button
                            className="p-4 bg-orange-500 hover:bg-orange-600 rounded-lg text-white transition"
                            onClick={() => handleRoleSelection('teacher')}
                        >
                            <div className="font-bold">Teacher</div>
                        </button>
                        <button
                            className="p-4 bg-orange-500 hover:bg-orange-600 rounded-lg text-white transition"
                            onClick={() => handleRoleSelection('student')}
                        >
                            <div className="font-bold">Student</div>
                        </button>
                    </div>
                )}

                {step === 1 && role === 'organization' && (
                    // Organization Registration
                    <form onSubmit={handleRegister} className="space-y-4">
                        <div>
                            <label className="block mb-1">Organization Name</label>
                            <input
                                type="text"
                                value={organizationName}
                                onChange={(e) => setOrganizationName(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">First Name</label>
                            <input
                                type="text"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Last Name</label>
                            <input
                                type="text"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Email</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Password</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full py-2 bg-orange-500 hover:bg-orange-600 rounded-lg text-white font-bold transition"
                        >
                            Register
                        </button>
                    </form>
                )}

                {step === 1 && (role === 'student' || role === 'teacher') && (
                    // Student/Teacher: Step 1 - Validate Unique Code
                    <form onSubmit={handleRegister} className="space-y-4">
                        <div>
                            <label className="block mb-1">Unique Code</label>
                            <input
                                type="text"
                                value={uniqueCode}
                                onChange={(e) => setUniqueCode(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full py-2 bg-orange-500 hover:bg-orange-600 rounded-lg text-white font-bold transition"
                        >
                            Validate Code
                        </button>
                    </form>
                )}

                {step === 2 && (role === 'student' || role === 'teacher') && (
                    // Student/Teacher: Step 2 - Email and Password Setup
                    <form onSubmit={handleRegister} className="space-y-4">
                        <div>
                            <label className="block mb-1">Email</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block mb-1">Password</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full px-3 py-2 rounded-md bg-neutral-700 border border-neutral-600 focus:outline-none focus:ring-2 focus:ring-orange-500"
                                required
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full py-2 bg-orange-500 hover:bg-orange-600 rounded-lg text-white font-bold transition"
                        >
                            Complete Registration
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default Register;
