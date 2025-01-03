import React from "react";
import { Outlet, useNavigate } from "react-router-dom";
import {
  FiUsers,
  FiBook,
  FiHome,
  FiCreditCard,
  FiCalendar,
  FiMessageCircle,
} from "react-icons/fi";
import Logout from "./Logout";

const AdminLayout: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="flex h-screen bg-neutral-900 text-white">
      {/* Sidebar */}
      <div className="w-48 flex flex-col items-center bg-neutral-800 text-white py-6 space-y-6 shadow-lg">
        <h1 className="text-xl font-bold mb-4">CtrlEdu Admin</h1>
        <div
          className="flex flex-col space-y-6 text-sm w-full"
          style={{ maxWidth: "150px" }}
        >
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/admin/home")}
          >
            <FiHome />
            Home
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/admin/people-tab")}
          >
            <FiUsers />
            People
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/admin/Module-tab")}
          >
            <FiCalendar />
            Module
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/admin/class-tab")}
          >
            <FiBook />
            Class
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/admin/Chat-tab")}
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

export default AdminLayout;
