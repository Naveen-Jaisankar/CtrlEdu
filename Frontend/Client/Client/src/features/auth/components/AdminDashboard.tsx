import React, { useState, useEffect } from "react";
import axios from "axios";
import Modal from "react-modal";
import { useNavigate } from "react-router-dom";
import {
  FiUsers,
  FiCalendar,
  FiHome,
  FiCreditCard,
  FiBook,
} from "react-icons/fi";

const AdminDashboard: React.FC = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("teacher");
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8081/api/admin/users",
        {
          headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        }
      );
      setUsers(response.data);
    } catch (error) {
      console.error("Failed to fetch users", error);
    }
  };

  const handleAddUser = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8081/api/admin/add-user",
        { firstName, lastName, email, role },
        { headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` } }
      );
      setSuccess(`User added successfully! Unique Code: ${response.data.uniqueCode}`);
      setIsModalOpen(false);
      fetchUsers();
    } catch (error) {
      setError("Failed to add user");
    }
  };

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
            onClick={() => navigate("/admin-dashboard")}
          >
            <FiHome />
            Dashboard
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/calendar")}
          >
            <FiCalendar />
            Calendar
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/people")}
          >
            <FiUsers />
            People
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/class")}
          >
            <FiBook />
            Class
          </button>
          <button
            className="flex items-center gap-2 hover:text-orange-500 w-full px-3"
            onClick={() => navigate("/payments")}
          >
            <FiCreditCard />
            Payments
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 p-8">
        <div className="flex justify-between items-center mb-8">
          <h2 className="text-2xl font-bold">People</h2>
          <button
            className="bg-orange-500 hover:bg-orange-700 text-white py-2 px-4 rounded"
            onClick={() => setIsModalOpen(true)}
          >
            Add
          </button>
        </div>
        <div className="bg-neutral-800 p-6 rounded shadow-md">
          <table className="w-full text-sm text-left text-gray-400">
            <thead className="text-xs text-gray-300 uppercase bg-neutral-700">
              <tr>
                <th scope="col" className="py-3 px-6">
                  Name
                </th>
                <th scope="col" className="py-3 px-6">
                  Email
                </th>
                <th scope="col" className="py-3 px-6">
                  Role
                </th>
              </tr>
            </thead>
            <tbody>
              {users.map((user, index) => (
                <tr
                  key={index}
                  className="border-b border-neutral-600 hover:bg-neutral-700"
                >
                  <td className="py-4 px-6">{`${user.firstName} ${user.lastName}`}</td>
                  <td className="py-4 px-6">{user.email}</td>
                  <td className="py-4 px-6">{user.role}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add User Modal */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        className="bg-neutral-800 w-1/3 mx-auto mt-20 p-6 rounded shadow-lg text-white"
        overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
        <h2 className="text-xl font-bold mb-4">Add User</h2>
        <form onSubmit={handleAddUser}>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-2">First Name</label>
            <input
              type="text"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
              required
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-2">Last Name</label>
            <input
              type="text"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
              required
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-2">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
              required
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-2">Role</label>
            <select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
            >
              <option value="teacher">Teacher</option>
              <option value="student">Student</option>
            </select>
          </div>
          <button
            type="submit"
            className="w-full bg-orange-500 hover:bg-orange-700 text-white py-2 px-4 rounded"
          >
            Submit
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default AdminDashboard;
