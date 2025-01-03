import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from '../../../services/authService';

const StudentDashboard: React.FC = () => {
    const [userData, setUserData] = useState<{ email: string } | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            const token = localStorage.getItem("accessToken");
            if (!token) {
                navigate("/login"); // Redirect if not authenticated
                return;
            }

            try {
                // Fetch user info using the token
                const user = await authService.getUserInfo(token);
                setUserData(user);
            } catch (error) {
                console.error("Error fetching user data", error);
                navigate("/login"); // Redirect on error
            }
        };

        fetchUserData();
    }, [navigate]);

    return (
        <div className="min-h-screen bg-gray-100 flex flex-col">
            <header className="bg-green-600 text-white p-4 shadow">
                <h1 className="text-2xl">Student Dashboard</h1>
            </header>
            <main className="flex-grow p-6">
                {userData ? (
                    <div>
                        <h2 className="text-xl">Welcome, {userData.email}</h2>
                        <p>Access your courses, grades, and assignments here.</p>
                        {/* Add specific student-related functionality here */}
                        <button
                            onClick={() => authService.logout() && navigate("/login")}
                            className="mt-4 px-4 py-2 bg-red-500 text-white rounded"
                        >
                            Logout
                        </button>
                    </div>
                ) : (
                    <p>Loading...</p>
                )}
            </main>
        </div>
    );
};

export default StudentDashboard;
