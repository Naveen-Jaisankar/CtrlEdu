import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./features/auth/components/Login";
import Register from "./features/auth/components/Register";
import AdminDashboard from "./features/auth/components/AdminDashboard";
import TeacherDashboard from "./features/auth/components/TeacherDashboard";
import StudentDashboard from "./features/auth/components/StudentDashboard";
import FirstLogin from "./features/auth/components/FirstLogin";
import LandingPage from "./features/landing/pages/LandingPage";

const App: React.FC = () => {
    // Helper function to check if the user is authenticated
    const isAuthenticated = (): boolean => {
        return !!localStorage.getItem("accessToken"); // Check if the access token exists
    };

    // Helper function to get the user's role
    const getUserRole = (): string | null => {
        return localStorage.getItem("role"); // Retrieve the stored role from localStorage
    };

    // Protected Route Component
    const ProtectedRoute: React.FC<{ children: React.ReactElement; role: string }> = ({ children, role }) => {
        if (!isAuthenticated()) {
            return <Navigate to="/login" replace />; // Redirect to login if not authenticated
        }

        const userRole = getUserRole();
        if (userRole !== role) {
            return <Navigate to="/" replace />; // Redirect to landing page if the role doesn't match
        }

        return children;
    };

    return (
        <Router>
            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<LandingPage />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/first-login" element={<FirstLogin />} />

                {/* Protected Routes */}
                <Route
                    path="/admin-dashboard"
                    element={
                        <ProtectedRoute role="super-admin">
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/teacher-dashboard"
                    element={
                        <ProtectedRoute role="teacher">
                            <TeacherDashboard />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/student-dashboard"
                    element={
                        <ProtectedRoute role="student">
                            <StudentDashboard />
                        </ProtectedRoute>
                    }
                />

                {/* Catch-All Route for Undefined Paths */}
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </Router>
    );
};

export default App;
