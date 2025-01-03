import React from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { FiBook, FiUsers, FiMessageCircle } from "react-icons/fi";
import Logout from "./Logout";

const TeacherDashboard: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="flex h-screen bg-neutral-900 text-white">
      {/* Sidebar */}
      <div className="w-48 flex flex-col items-center bg-neutral-800 text-white py-6 space-y-6 shadow-lg">
        <h1 className="text-xl font-bold mb-4">CtrlEdu Teacher</h1>
        <div
          className="flex flex-col space-y-6 text-sm w-full"
          style={{ maxWidth: "150px" }}
        >
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/teacher/modules")}
          >
            <FiBook />
            Modules
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/teacher/students")}
          >
            <FiUsers />
            Students
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/teacher-dashboard/Chat-tab")}
          >
            <FiMessageCircle />
            Messages
          </button>
          <div className="mt-auto">
            <Logout />
          </div>
        </div>
      </div>

      {/* Main Content Area */}
      <div className="flex-1 p-6">
        <Outlet />
      </div>
    </div>
  );
};

export default TeacherDashboard;
