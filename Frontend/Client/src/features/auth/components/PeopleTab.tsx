import React, { useState, useEffect } from "react";
import axios from "axios";
import Modal from "react-modal";
import { toast } from "react-toastify";

const PeopleTab: React.FC = () => {
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const usersPerPage = 5;
  const [sortColumn, setSortColumn] = useState("userFirstName");
  const [sortDirection, setSortDirection] = useState("asc");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("teacher");
  const [editingUser, setEditingUser] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/admin/users", {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      setUsers(response.data);
    } catch (error) {
      console.error("Failed to fetch users", error);
      toast.error("Failed to fetch users.");
    }
  };

  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  };

  const handleSort = (column: string) => {
    if (sortColumn === column) {
      setSortDirection(sortDirection === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortDirection("asc");
    }
  };

  const sortedUsers = [...users].sort((a, b) => {
    const valueA = a[sortColumn]?.toString().toLowerCase() || "";
    const valueB = b[sortColumn]?.toString().toLowerCase() || "";
    const comparison = valueA.localeCompare(valueB);
    return sortDirection === "asc" ? comparison : -comparison;
  });

  const filteredUsers = sortedUsers.filter((user) => {
    const fullName = `${user.userFirstName} ${user.userLastName}`.toLowerCase();
    const email = user.userEmail.toLowerCase();
    return (
      fullName.includes(searchTerm.toLowerCase()) || email.includes(searchTerm.toLowerCase())
    );
  });

  const indexOfLastUser = currentPage * usersPerPage;
  const indexOfFirstUser = indexOfLastUser - usersPerPage;
  const currentUsers = filteredUsers.slice(indexOfFirstUser, indexOfLastUser);
  const totalPages = Math.ceil(filteredUsers.length / usersPerPage);
  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1);

  const handleEditClick = (user: any) => {
    setEditingUser(user);
    setFirstName(user.userFirstName);
    setLastName(user.userLastName);
    setEmail(user.userEmail);
    setRole(user.userRole);
    setIsModalOpen(true);
  };

  const handleDeleteUser = async (userId: number) => {
    try {
      await axios.delete(`http://localhost:8081/api/admin/delete-user/${userId}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      toast.success("User deleted successfully!");
      fetchUsers();
    } catch (error) {
      toast.error("Failed to delete user.");
    }
  };

  const handleAddOrEditUser = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingUser) {
        await axios.put(
          `http://localhost:8081/api/admin/edit-user/${editingUser.userId}`,
          { firstName, lastName, email, role },
          { headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` } }
        );
        toast.success("User updated successfully!");
      } else {
        await axios.post(
          "http://localhost:8081/api/admin/add-user",
          { firstName, lastName, email, role },
          { headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` } }
        );
        toast.success("User added successfully!");
      }
      setIsModalOpen(false);
      fetchUsers();
    } catch (error) {
      toast.error(editingUser ? "Failed to update user" : "Failed to add user");
    } finally {
      setEditingUser(null);
    }
  };

  const resetForm = () => {
    setFirstName("");
    setLastName("");
    setEmail("");
    setRole("teacher");
    setEditingUser(null);
  };

  const handleCloseModal = () => {
    resetForm();
    setIsModalOpen(false);
  };

  return (
    <><div className="flex-1 p-8">
          <div className="flex justify-between items-center mb-8">
              <h2 className="text-2xl font-bold">People</h2>
              <div>
                  <input
                      type="text"
                      placeholder="Search users..."
                      value={searchTerm}
                      onChange={(e) => {
                          setSearchTerm(e.target.value);
                          setCurrentPage(1); // Reset to the first page when search term changes
                      } }
                      className="w-full p-3 mb-4 border border-gray-600 rounded-md shadow-sm bg-neutral-700 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500" />
              </div>
              <button
                  className="bg-orange-500 hover:bg-orange-700 text-white py-2 px-4 rounded"
                  onClick={() => setIsModalOpen(true)}
              >
                  Add
              </button>

          </div>
          <div className="bg-neutral-800 p-6 rounded shadow-md">
              <table className="w-full table-fixed text-sm text-left text-gray-400">
                  <thead className="text-xs text-gray-300 uppercase bg-neutral-700">
                      <tr>
                          <th
                              className="py-3 px-6 cursor-pointer"
                              onClick={() => handleSort("userFirstName")}
                          >
                              Name {sortColumn === "userFirstName" && (sortDirection === "asc" ? "▲" : "▼")}
                          </th>
                          <th
                              className="py-3 px-6 cursor-pointer"
                              onClick={() => handleSort("userEmail")}
                          >
                              Email {sortColumn === "userEmail" && (sortDirection === "asc" ? "▲" : "▼")}
                          </th>
                          <th
                              className="py-3 px-6 cursor-pointer"
                              onClick={() => handleSort("userRole")}
                          >
                              Role {sortColumn === "userRole" && (sortDirection === "asc" ? "▲" : "▼")}
                          </th>
                          <th className="py-3 px-6">Actions</th>
                      </tr>
                  </thead>
                  <tbody>
                      {currentUsers.length > 0 ? (
                          currentUsers.map((user, index) => (
                              <tr key={index} className="border-b border-neutral-600 hover:bg-neutral-700">
                                  <td className="py-4 px-6">{`${user.userFirstName} ${user.userLastName}`}</td>
                                  <td className="py-4 px-6">{user.userEmail}</td>
                                  <td className="py-4 px-6">{user.userRole}</td>
                                  <td className="py-4 px-6 flex gap-2">
                                      <button
                                          onClick={() => handleEditClick(user)}
                                          className="bg-blue-500 hover:bg-blue-700 text-white py-1 px-3 rounded"
                                      >
                                          Edit
                                      </button>
                                      <button
                                          onClick={() => handleDeleteUser(user.userId)}
                                          className="bg-red-500 hover:bg-red-700 text-white py-1 px-3 rounded"
                                      >
                                          Delete
                                      </button>
                                  </td>
                              </tr>
                          ))
                      ) : (
                          <tr>
                              <td colSpan="4" className="py-4 px-6 text-center text-gray-400">
                                  No users found.
                              </td>
                          </tr>
                      )}
                  </tbody>
              </table>
          </div>
          <div className="flex justify-center mt-4">
              {pageNumbers.map((number) => (
                  <button
                      key={number}
                      onClick={() => handlePageChange(number)}
                      className={`px-3 py-1 mx-1 rounded ${currentPage === number ? "bg-orange-500 text-white" : "bg-gray-700 text-gray-300"}`}
                  >
                      {number}
                  </button>
              ))}
          </div>
      </div><Modal
          isOpen={isModalOpen}
          onRequestClose={handleCloseModal}
          className="bg-neutral-800 w-1/3 mx-auto mt-20 p-6 rounded shadow-lg text-white"
          overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      >
              <h2 className="text-xl font-bold mb-4">{editingUser ? "Edit User" : "Add User"}</h2>
              <form onSubmit={handleAddOrEditUser}>
                  <div className="mb-4">
                      <label className="block text-sm font-medium mb-2">First Name</label>
                      <input
                          type="text"
                          value={firstName}
                          onChange={(e) => setFirstName(e.target.value)}
                          className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
                          required />
                  </div>
                  <div className="mb-4">
                      <label className="block text-sm font-medium mb-2">Last Name</label>
                      <input
                          type="text"
                          value={lastName}
                          onChange={(e) => setLastName(e.target.value)}
                          className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
                          required />
                  </div>
                  <div className="mb-4">
                      <label className="block text-sm font-medium mb-2">Email</label>
                      <input
                          type="email"
                          value={email}
                          onChange={(e) => setEmail(e.target.value)}
                          className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
                          required />
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
          </Modal></>
                );
              };
              
              export default PeopleTab;
              
 

