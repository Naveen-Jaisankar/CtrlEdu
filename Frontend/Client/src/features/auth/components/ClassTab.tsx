import React, { useState, useEffect } from "react";
import axios from "axios";
import Modal from "react-modal";
import { toast } from "react-toastify";

const ClassTab: React.FC = () => {
  const [classes, setClasses] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const classesPerPage = 5;
  const [sortColumn, setSortColumn] = useState("className");
  const [sortDirection, setSortDirection] = useState("asc");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [className, setClassName] = useState("");
  const [teacherSearch, setTeacherSearch] = useState("");
  const [teacherSuggestions, setTeacherSuggestions] = useState([]);
  const [teacher, setTeacher] = useState("");
  const [editingClass, setEditingClass] = useState(null);

  useEffect(() => {
    fetchClasses();
    fetchTeachers();
  }, []);

  const fetchClasses = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/admin/classes", {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      setClasses(response.data);
    } catch (error) {
      toast.error("Failed to fetch classes.");
    }
  };

  const fetchTeachers = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/admin/teachers", {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      setTeacherSuggestions(response.data);
    } catch (error) {
      toast.error("Failed to fetch teachers.");
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

  const sortedClasses = [...classes].sort((a, b) => {
    const valueA = a[sortColumn]?.toString().toLowerCase() || "";
    const valueB = b[sortColumn]?.toString().toLowerCase() || "";
    const comparison = valueA.localeCompare(valueB);
    return sortDirection === "asc" ? comparison : -comparison;
  });

  const filteredClasses = sortedClasses.filter((cls) =>
    cls.className.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastClass = currentPage * classesPerPage;
  const indexOfFirstClass = indexOfLastClass - classesPerPage;
  const currentClasses = filteredClasses.slice(indexOfFirstClass, indexOfLastClass);

  const totalPages = Math.ceil(filteredClasses.length / classesPerPage);
  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1);

  const handleTeacherSearch = (searchValue: string) => {
    setTeacherSearch(searchValue);
    const filtered = teacherSuggestions.filter((teacher) =>
      teacher.name.toLowerCase().includes(searchValue.toLowerCase())
    );
    setTeacherSuggestions(filtered);
  };

  const validateTeacher = () => {
    const matchedTeacher = teacherSuggestions.find(
      (teacher) => teacher.name.toLowerCase() === teacherSearch.toLowerCase()
    );
    if (!matchedTeacher) {
      toast.error("Please select a valid teacher from the suggestions.");
      return false;
    }
    setTeacher(matchedTeacher.id);
    return true;
  };

  const handleEditClick = (cls: any) => {
    setEditingClass(cls);
    setClassName(cls.className);
    setTeacherSearch(cls.teacherName);
    setIsModalOpen(true);
  };

  const handleDeleteClass = async (classId: number) => {
    try {
      await axios.delete(`http://localhost:8081/api/admin/delete-class/${classId}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      toast.success("Class deleted successfully!");
      fetchClasses();
    } catch (error) {
      toast.error("Failed to delete class.");
    }
  };

  const handleAddOrEditClass = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateTeacher()) return;
    try {
      const payload = { className, teacherId: teacher };
      if (editingClass) {
        await axios.put(
          `http://localhost:8081/api/admin/edit-class/${editingClass.classId}`,
          payload,
          { headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` } }
        );
        toast.success("Class updated successfully!");
      } else {
        await axios.post("http://localhost:8081/api/admin/add-class", payload, {
          headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        toast.success("Class added successfully!");
      }
      setIsModalOpen(false);
      fetchClasses();
    } catch (error) {
      toast.error(editingClass ? "Failed to update class" : "Failed to add class");
    } finally {
      resetForm();
    }
  };

  const resetForm = () => {
    setClassName("");
    setTeacherSearch("");
    setTeacher("");
    setEditingClass(null);
  };

  const handleCloseModal = () => {
    resetForm();
    setIsModalOpen(false);
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Classes</h2>
      <div className="mb-4">
        <input
          type="text"
          placeholder="Search classes..."
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
            setCurrentPage(1);
          }}
          className="w-full p-3 border rounded-md bg-neutral-700 text-white placeholder-gray-400"
        />
      </div>
      <button
        className="bg-orange-500 hover:bg-orange-700 text-white py-2 px-4 rounded mb-4"
        onClick={() => setIsModalOpen(true)}
      >
        Add Class
      </button>
      <table className="w-full text-left">
        <thead className="bg-neutral-800 text-gray-300">
          <tr>
            <th
              className="py-2 px-4 cursor-pointer"
              onClick={() => handleSort("className")}
            >
              Name {sortColumn === "className" && (sortDirection === "asc" ? "▲" : "▼")}
            </th>
            <th
              className="py-2 px-4 cursor-pointer"
              onClick={() => handleSort("teacherName")}
            >
              Teacher {sortColumn === "teacherName" && (sortDirection === "asc" ? "▲" : "▼")}
            </th>
            <th className="py-2 px-4">Actions</th>
          </tr>
        </thead>
        <tbody>
          {currentClasses.map((cls, index) => (
            <tr
              key={index}
              className="border-b border-neutral-600 hover:bg-neutral-700"
            >
              <td className="py-4 px-6">{cls.className}</td>
              <td className="py-4 px-6">{cls.teacherName}</td>
              <td className="py-4 px-6 flex gap-2">
                <button
                  onClick={() => handleEditClick(cls)}
                  className="bg-blue-500 hover:bg-blue-700 text-white py-1 px-3 rounded"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDeleteClass(cls.classId)}
                  className="bg-red-500 hover:bg-red-700 text-white py-1 px-3 rounded"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="mt-4 flex justify-center gap-2">
        {pageNumbers.map((number) => (
          <button
            key={number}
            onClick={() => handlePageChange(number)}
            className={`py-1 px-3 rounded ${
              currentPage === number ? "bg-orange-500 text-white" : "bg-neutral-700 text-gray-300"
            } hover:bg-orange-600`}
          >
            {number}
          </button>
        ))}
      </div>
      <Modal
  isOpen={isModalOpen}
  onRequestClose={handleCloseModal}
  className="bg-neutral-800 w-1/3 mx-auto mt-20 p-6 rounded shadow-lg text-white"
  overlayClassName="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
>
  <h2 className="text-xl font-bold mb-4">{editingClass ? "Edit Class" : "Add Class"}</h2>
  <form onSubmit={handleAddOrEditClass}>
    <div className="mb-4">
      <label className="block text-sm font-medium mb-2">Class Name</label>
      <input
        type="text"
        value={className}
        onChange={(e) => setClassName(e.target.value)}
        className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
        required
      />
    </div>
    <div className="mb-4 relative">
      <label className="block text-sm font-medium mb-2">Teacher</label>
      <input
        type="text"
        value={teacherSearch}
        onChange={(e) => handleTeacherSearch(e.target.value)}
        className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
        placeholder="Type teacher's name"
        required
      />
      {teacherSearch && teacherSuggestions.length > 0 && (
        <ul className="absolute z-10 bg-neutral-800 text-white w-full rounded shadow-lg max-h-40 overflow-auto">
          {teacherSuggestions.map((teacher) => (
            <li
              key={teacher.id}
              onClick={() => {
                setTeacherSearch(teacher.name);
                setTeacher(teacher.id);
              }}
              className="py-2 px-4 hover:bg-neutral-600 cursor-pointer"
            >
              {teacher.name}
            </li>
          ))}
        </ul>
      )}
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

export default ClassTab;
                   
