import React, { useState, useEffect } from "react";
import axios from "axios";
import Modal from "react-modal";
import { toast } from "react-toastify";

  const ModuleTab: React.FC = () => {
  const [modules, setModules] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const modulesPerPage = 5;
  const [sortColumn, setSortColumn] = useState("moduleCode");
  const [sortDirection, setSortDirection] = useState("asc");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [moduleCode, setModuleCode] = useState("");
  const [moduleName, setModuleName] = useState("");
  const [teacherSuggestions, setTeacherSuggestions] = useState([]);
  const [teacher, setTeacher] = useState("");
  const [editingModule, setEditingModule] = useState(null);
  const [allTeachers, setAllTeachers] = useState([]);
  const [currentTeacherId, setCurrentTeacherId] = useState(null);
  const [currentTeacherFirstName, setCurrentTeacherFirstName] = useState("");
  const [currentTeacherLastName, setCurrentTeacherLastName] = useState("");


useEffect(() => {
    fetchModules();
}, []);

  useEffect(() => {
    if (isModalOpen) {
      if (editingModule) {
        fetchAllTeachersForEdit();
      } else {
        fetchAvailableTeachersForAdd();
      }
    }
  }, [isModalOpen, editingModule]);

  useEffect(() => {
    if (currentTeacherId && !allTeachers.some((teacher) => teacher.teacherId === currentTeacherId)) {
        const currentTeacher = {
            teacherId: currentTeacherId,
            firstName: currentTeacherFirstName,
            lastName: currentTeacherLastName,
            isAssigned: false, // Ensure the current teacher remains selectable
        };
        setAllTeachers((prev) => [...prev, currentTeacher]);
    }
}, [allTeachers, currentTeacherId, currentTeacherFirstName, currentTeacherLastName]);

const fetchModules = async () => {
    try {
        const response = await axios.get("http://localhost:8081/api/admin/modules", {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        setModules(response.data); // Module data is stored in the `modules` state
    } catch (error) {
        toast.error("Failed to fetch modules.");
    }
};


  // Fetch unassigned teachers for add
  const fetchAvailableTeachersForAdd = async () => {
    try {
        const response = await axios.get(`http://localhost:8081/api/admin/available-teachers`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        setTeacherSuggestions(response.data);
    } catch (error) {
        toast.error("Failed to fetch available teachers.");
    }
};

const fetchAllTeachersForEdit = async () => {
    if (!editingModule || !editingModule.teacherId) {
        return;
    }

    const currentTeacherId = editingModule.teacherId;

    try {
        const response = await axios.get(
            `http://localhost:8081/api/admin/all-teachers?currentTeacherId=${currentTeacherId}`,
            {
                headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
            }
        );

        // Ensure the current teacher is included in the list
        const teachers = response.data;

        // If the current teacher is not in the list, add it
        const currentTeacher = {
            teacherId: currentTeacherId,
            firstName: editingModule.teacherFirstName,
            lastName: editingModule.teacherLastName,
            isAssigned: true, // Mark as assigned, so it’s selectable
        };

        // Check if the current teacher is already in the list, otherwise, add it
        const updatedTeachers = teachers.some(teacher => teacher.teacherId === currentTeacher.teacherId)
            ? teachers
            : [currentTeacher, ...teachers];

        setAllTeachers(updatedTeachers); // Update the teacher suggestions list

        // Set the teacher suggestions
        setTeacherSuggestions(updatedTeachers);

    } catch (error) {
        console.error("Failed to fetch all teachers:", error);
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

  const sortedModules = [...modules].sort((a, b) => {
    const valueA = a[sortColumn]?.toString().toLowerCase() || "";
    const valueB = b[sortColumn]?.toString().toLowerCase() || "";
    const comparison = valueA.localeCompare(valueB);
    return sortDirection === "asc" ? comparison : -comparison;
  });

  const filteredModules = sortedModules.filter((mod) =>
    mod.moduleName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastModule = currentPage * modulesPerPage;
  const indexOfFirstModule = indexOfLastModule - modulesPerPage;
  const currentModules = filteredModules.slice(indexOfFirstModule, indexOfLastModule);
  const totalPages = Math.ceil(filteredModules.length / modulesPerPage);
  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1);

  const handleEditClick = (mod) => {
    setEditingModule(mod);
    setModuleCode(mod.moduleCode);
    setModuleName(mod.moduleName);
    setCurrentTeacherId(mod.teacherId); // Ensure this is correct
    setCurrentTeacherFirstName(mod.teacherFirstName); // Ensure this is correct
    setCurrentTeacherLastName(mod.teacherLastName); // Ensure this is correct
    setIsModalOpen(true);
};

  const handleDeleteModule = async (moduleId: number) => {
    try {
      await axios.delete(`http://localhost:8081/api/admin/delete-module/${moduleId}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      toast.success("Module deleted successfully!");
      fetchModules();
    } catch (error) {
      toast.error("Failed to delete module.");
    }
  };

  const handleAddOrEditModule = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
        const payload = { moduleCode, moduleName, teacherId: teacher };

        if (editingModule) {
            // Check for duplicate teacher assignment
            const isDuplicate = modules.some(
                (mod) => mod.teacherId === teacher && mod.moduleId !== editingModule.moduleId
            );
            if (isDuplicate) {
                toast.error("This teacher is already assigned to another module.");
                return;
            }

            // Update the module
            await axios.put(
                `http://localhost:8081/api/admin/edit-module/${editingModule.moduleId}`,
                payload,
                { headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` } }
            );
            toast.success("Module updated successfully!");
        } else {
            // Add a new module
            await axios.post("http://localhost:8081/api/admin/add-module", payload, {
                headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
            });
            toast.success("Module added successfully!");
        }

        setIsModalOpen(false);
        fetchModules(); // Refresh the module list
    } catch (error) {
        toast.error(error.response?.data?.message || "Failed to save module");
    } finally {
        resetForm(); // Reset form state
    }
};
  const resetForm = () => {
    setModuleCode("");
    setModuleName("");
    setTeacher("");
    setEditingModule(null);
  };

  const handleCloseModal = () => {
    resetForm();
    setIsModalOpen(false);
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Modules</h2>
      <div className="mb-4">
        <input
          type="text"
          placeholder="Search modules..."
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
        Add Module
      </button>
      <table className="w-full text-left">
        <thead className="bg-neutral-800 text-gray-300">
          <tr>
            <th
              className="py-2 px-4 cursor-pointer"
              onClick={() => handleSort("moduleCode")}
            >
              Module Code {sortColumn === "moduleCode" && (sortDirection === "asc" ? "▲" : "▼")}
            </th>
            <th
              className="py-2 px-4 cursor-pointer"
              onClick={() => handleSort("moduleName")}
            >
              Module Name {sortColumn === "moduleName" && (sortDirection === "asc" ? "▲" : "▼")}
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
          {currentModules.map((mod, index) => (
            <tr
              key={index}
              className="border-b border-neutral-600 hover:bg-neutral-700"
            >
              <td className="py-4 px-6">{mod.moduleCode}</td>
              <td className="py-4 px-6">{mod.moduleName}</td>
              <td className="py-4 px-6">{mod.teacherName}</td>
              <td className="py-4 px-6 flex gap-2">
                <button
                  onClick={() => handleEditClick(mod)}
                  className="bg-blue-500 hover:bg-blue-700 text-white py-1 px-3 rounded"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDeleteModule(mod.moduleId)}
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
        className="bg-neutral-800 text-white p-6 rounded shadow-md max-w-md mx-auto mt-20"
      >
        <h2 className="text-lg font-bold mb-4">
          {editingModule ? "Edit Module" : "Add Module"}
        </h2>
        <form onSubmit={handleAddOrEditModule}>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-1">Module Code</label>
            <input
              type="text"
              value={moduleCode}
              onChange={(e) => setModuleCode(e.target.value)}
              className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
              required
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-1">Module Name</label>
            <input
              type="text"
              value={moduleName}
              onChange={(e) => setModuleName(e.target.value)}
              className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
              required
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-1">Teacher</label>
            <select
                value={teacher}
                onChange={(e) => setTeacher(e.target.value)}
                className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
                required
            >
                <option value="" disabled>Select a teacher</option>
                {teacherSuggestions.map((teacher) => (
                    <option key={teacher.teacherId} value={teacher.teacherId}>
                        {teacher.firstName} {teacher.lastName}
                    </option>
                ))}
            </select>
            </div>
          <div className="flex justify-end gap-2">
            <button
              type="button"
              onClick={handleCloseModal}
              className="bg-gray-500 hover:bg-gray-700 text-white py-1 px-3 rounded"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-orange-500 hover:bg-orange-700 text-white py-1 px-3 rounded"
            >
              {editingModule ? "Update" : "Add"}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default ModuleTab;
