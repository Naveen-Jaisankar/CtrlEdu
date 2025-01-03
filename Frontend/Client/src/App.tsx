import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Login from "./features/auth/components/Login";
import Register from "./features/auth/components/Register";
import AdminLayout from "./features/auth/components/AdminLayout";
import TeacherDashboard from "./features/auth/components/TeacherDashboard";
import StudentDashboard from "./features/auth/components/StudentDashboard";
import FirstLogin from "./features/auth/components/FirstLogin";
import LandingPage from "./features/landing/pages/LandingPage";
import ChatPage from "./features/chat/pages/ChatPage";
import ClassTab from "./features/auth/components/ClassTab";
import PeopleTab from "./features/auth/components/PeopleTab";
import ModuleTab from "./features/auth/components/ModuleTab";

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
  const ProtectedRoute: React.FC<{
    children: React.ReactElement;
    roles?: string[];
  }> = ({ children, roles }) => {
    if (!isAuthenticated()) {
      return <Navigate to="/login" replace />;
    }

    if (roles && !roles.includes(getUserRole() || "")) {
      return <Navigate to="/" replace />;
    }

    return children;
  };

  return (
    <Router>
      <ToastContainer position="top-right" autoClose={3000} />
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/first-login" element={<FirstLogin />} />

        {/* Protected Routes */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute roles={["super-admin"]}>
              <AdminLayout />
            </ProtectedRoute>
          }
        >
          <Route path="people-tab" element={<PeopleTab />} />
          <Route path="class-tab" element={<ClassTab />} />
          <Route path="Module-tab" element={<ModuleTab />} />
          <Route path="Chat-tab" element={<ChatPage />} />
        </Route>
        <Route
          path="/teacher-dashboard"
          element={
            <ProtectedRoute roles={["teacher"]}>
              <TeacherDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/student-dashboard"
          element={
            <ProtectedRoute roles={["student"]}>
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
