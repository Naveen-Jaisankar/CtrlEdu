// ClassTab.tsx
import React, { useState, useEffect } from "react";
import axios from "axios";
import Modal from "react-modal";
import { toast } from "react-toastify";

const ClassTab: React.FC = () => {
  const [classes, setClasses] = useState([]);
  const [modules, setModules] = useState([]);
  const [className, setClassName] = useState("");
  const [numStudents, setNumStudents] = useState(0);
  const [selectedModules, setSelectedModules] = useState<number[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [sortColumn, setSortColumn] = useState("className");
  const [sortDirection, setSortDirection] = useState("asc");
  const [classList, setClassList] = useState([]); // Initialize with an empty array
  const [moduleSuggestions, setModuleSuggestions] = useState([]); // Initialize as empty

  useEffect(() => {
    fetchClasses();
    fetchModules();
  }, []);

  useEffect(() => {
    fetchModulesForDropdown();
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

  const fetchClassData = async () => {
    try {
      const response = await axios.get("/api/classes");
      setClassList(response.data || []); // Ensure state is an array
    } catch (error) {
      console.error("Failed to fetch classes:", error);
      setClassList([]); // Set as empty to avoid undefined issues
    }
  };
  

  const fetchModules = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/admin/modules", {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      setModules(response.data);
    } catch (error) {
      toast.error("Failed to fetch modules.");
    }
  };

  const fetchModulesForDropdown = async () => {
    try {
        const response = await axios.get("http://localhost:8081/api/admin/modules", {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        setModuleSuggestions(response.data); // Populate the state with module data
    } catch (error) {
        toast.error("Failed to fetch modules for dropdown.");
    }
};


  const handleAddClass = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const payload = {
        className,
        numStudents,
        moduleIds: selectedModules,
      };
      await axios.post("http://localhost:8081/api/admin/add-class", payload, {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      toast.success("Class added successfully!");
      setIsModalOpen(false);
      fetchClasses();
      resetForm();
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to add class.");
    }
  };

  const resetForm = () => {
    setClassName("");
    setNumStudents(0);
    setSelectedModules([]);
  };

  const handleCloseModal = () => {
    resetForm();
    setIsModalOpen(false);
  };

  const handleModuleSelection = (moduleId: number) => {
    if (selectedModules.includes(moduleId)) {
        setSelectedModules(selectedModules.filter((id) => id !== moduleId));
    } else {
        setSelectedModules([...selectedModules, moduleId]);
    }
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
  return sortDirection === "asc"
    ? valueA.localeCompare(valueB)
    : valueB.localeCompare(valueA);
});

const filteredClasses = sortedClasses.filter((cls) =>
  cls.className.toLowerCase().includes(searchTerm.toLowerCase())
);

useEffect(() => {
  console.log("Filtered classes:", filteredClasses); // Debugging output
}, [filteredClasses]);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Classes</h2>
      <div className="mb-4">
      <input
          type="text"
          placeholder="Search classes..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
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
              Class Name {sortColumn === "className" && (sortDirection === "asc" ? "▲" : "▼")}
            </th>
            <th
              className="py-2 px-4 cursor-pointer"
              onClick={() => handleSort("numberOfStudents")}
            >
              Number of Students {sortColumn === "numberOfStudents" && (sortDirection === "asc" ? "▲" : "▼")}
            </th>
            <th
              className="py-2 px-4 cursor-pointer"
              onClick={() => handleSort("moduleNames")}
            >
              Modules {sortColumn === "moduleNames" && (sortDirection === "asc" ? "▲" : "▼")}
            </th>

          </tr>
        </thead>
        <tbody>
          {filteredClasses.length > 0 ? (
            filteredClasses.map((cls) => (
              <tr key={cls.classId}>
                <td>{cls.className}</td>
                <td>{cls.numStudents}</td>
                <td>{cls.moduleNames.join(", ")}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={3}>No classes available</td>
            </tr>
          )}
        </tbody>
      </table>

      <Modal
  isOpen={isModalOpen}
  onRequestClose={handleCloseModal}
  className="bg-neutral-800 text-white p-6 rounded shadow-md max-w-md mx-auto mt-20"
>
  <h2 className="text-lg font-bold mb-4">Add Class</h2>
  <form onSubmit={handleAddClass}>
    <div className="mb-4">
      <label className="block text-sm font-medium mb-1">Class Name</label>
      <input
        type="text"
        value={className}
        onChange={(e) => setClassName(e.target.value)}
        className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
        required
      />
    </div>
    <div className="mb-4">
      <label className="block text-sm font-medium mb-1">Number of Students</label>
      <input
        type="number"
        value={numStudents}
        onChange={(e) => setNumStudents(Number(e.target.value))}
        className="w-full border rounded py-2 px-3 bg-neutral-700 text-white"
        required
      />
    </div>
    <div className="mb-4">
    <label className="block text-sm font-medium mb-1">Modules</label>
    <div className="border rounded py-2 px-3 bg-neutral-700 text-white">
        {moduleSuggestions.map((module) => (
            <div key={module.moduleId} className="flex items-center">
                <input
                    type="checkbox"
                    id={`module-${module.moduleId}`}
                    checked={selectedModules.includes(module.moduleId)}
                    onChange={() => handleModuleSelection(module.moduleId)}
                    className="mr-2"
                />
                <label htmlFor={`module-${module.moduleId}`}>
                    {module.moduleName}
                </label>
            </div>
        ))}
    </div>
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
        Add Class
      </button>
    </div>
  </form>
</Modal>

    </div>
  );
};

export default ClassTab;
