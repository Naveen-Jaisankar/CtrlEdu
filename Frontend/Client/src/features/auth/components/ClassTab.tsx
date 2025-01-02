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
  const [students, setStudents] = useState([]);
  const [selectedStudents, setSelectedStudents] = useState([]);
  const [availableStudents, setAvailableStudents] = useState([]);


  useEffect(() => {
    fetchClasses();
    fetchModules();
  }, []);

  useEffect(() => {
    fetchModulesForDropdown();
}, []);

useEffect(() => {
  fetchStudents();
}, []);



  const fetchClasses = async () => {
    try {
      const response = await axios.get("http://localhost:8084/api/admin/classes", {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      setClasses(response.data);
    } catch (error) {
      toast.error("Failed to fetch classes.");
    }
  };

  const handleStudentSelection = (student) => {
    if (selectedStudents.length >= numStudents) {
        toast.error(`You can only select up to ${numStudents} students.`);
        return;
    }
    setAvailableStudents(availableStudents.filter((s) => s.id !== student.id));
    setSelectedStudents([...selectedStudents, student]);
};


const handleStudentRemoval = (student) => {
    setSelectedStudents(selectedStudents.filter((s) => s.id !== student.id));
    setAvailableStudents([...availableStudents, student]);
};




  const payload = {
    className,
    moduleIds: selectedModules,
    studentIds: selectedStudents.map((s) => s.id),
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

  const fetchStudents = async () => {
    try {
        const response = await axios.get("http://localhost:8084/api/admin/students", {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        const students = response.data.map((student) => ({
            id: student.studentId,
            firstName: student.firstName,
            lastName: student.lastName,
            isAssigned: student.isAssigned,
        }));
        setAvailableStudents(students.filter((s) => !s.isAssigned));
        setSelectedStudents(students.filter((s) => s.isAssigned));
    } catch (error) {
        console.error("Error fetching students:", error);
        toast.error("Failed to fetch students.");
    }
};



  const fetchModules = async () => {
    try {
      const response = await axios.get("http://localhost:8084/api/admin/modules", {
        headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      setModules(response.data);
    } catch (error) {
      toast.error("Failed to fetch modules.");
    }
  };

  const fetchModulesForDropdown = async () => {
    try {
        const response = await axios.get("http://localhost:8084/api/admin/modules", {
            headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
        });
        setModuleSuggestions(response.data); // Populate the state with module data
    } catch (error) {
        toast.error("Failed to fetch modules for dropdown.");
    }
};


const handleAddClass = async (e: React.FormEvent) => {
  e.preventDefault();
  if (selectedStudents.length > numStudents) {
      toast.error("The number of selected students exceeds the allowed limit.");
      return;
  }
  console.log("handleAddClass triggered");
  try {
      const payload = {
          className,
          numberOfStudents: numStudents, // Fix key name
          moduleIds: selectedModules,
          studentIds: selectedStudents.map((s) => s.id),
      };
      console.log("Payload:", payload);

      await axios.post("http://localhost:8084/api/admin/add-class", payload, {
          headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      });
      toast.success("Class added successfully!");
      setIsModalOpen(false);
      fetchClasses();
      fetchStudents();
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
  const valueA = a[sortColumn];
  const valueB = b[sortColumn];

  if (typeof valueA === "number" && typeof valueB === "number") {
    // Numeric sorting
    return sortDirection === "asc" ? valueA - valueB : valueB - valueA;
  } else {
    // String sorting
    const stringValueA = valueA?.toString().toLowerCase() || "";
    const stringValueB = valueB?.toString().toLowerCase() || "";
    return sortDirection === "asc"
      ? stringValueA.localeCompare(stringValueB)
      : stringValueB.localeCompare(stringValueA);
  }
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
              onClick={() => handleSort("numStudents")} // Use "numStudents" as the column key
            >
              Number of Students {sortColumn === "numStudents" && (sortDirection === "asc" ? "▲" : "▼")}
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
<label>Students:</label>
<div>
    <p>Remaining slots: {numStudents - selectedStudents.length}</p>
</div>
<div style={{ color: selectedStudents.length >= numStudents ? "red" : "black" }}>
    {selectedStudents.length >= numStudents
        ? "You have reached the maximum number of students."
        : `You can select ${numStudents - selectedStudents.length} more students.`}
</div>

<div className="dual-list-box-container" style={{ display: "flex", gap: "20px" }}>
  {/* Available Students */}
  {/* Available Students */}
<div style={{ flex: 1 }}>
  <h3>Available Students</h3>
  <select
    multiple
    value={[]}
    style={{ width: "100%", height: "200px" }}
    onChange={(e) => {
      const selectedOptions = Array.from(e.target.selectedOptions).map((option) => JSON.parse(option.value));
      selectedOptions.forEach((student) => handleStudentSelection(student));
    }}
  >
    {availableStudents.map((student) => (
      <option key={student.id} value={JSON.stringify(student)}>
        {student.firstName} {student.lastName}
      </option>
    ))}
  </select>
</div>


  {/* Transfer Buttons */}
  <div style={{ display: "flex", flexDirection: "column", justifyContent: "center", gap: "10px" }}>
    <button
      onClick={() =>
        availableStudents.forEach((student) => handleStudentSelection(student))
      }
    >
      &gt;&gt;
    </button>
    <button
      onClick={() =>
        selectedStudents.forEach((student) => handleStudentRemoval(student))
      }
    >
      &lt;&lt;
    </button>
  </div>

  {/* Selected Students */}
  <div style={{ flex: 1 }}>
    <h3>Selected Students</h3>
    <select
      multiple
      style={{ width: "100%", height: "200px" }}
      onChange={(e) => {
        const selectedOptions = Array.from(e.target.selectedOptions).map(
          (option) => JSON.parse(option.value)
        );
        selectedOptions.forEach((student) => handleStudentRemoval(student));
      }}
    >
      {selectedStudents.map((student) => (
        <option
          key={student.id}
          value={JSON.stringify(student)}
        >
          {student.firstName} {student.lastName}
        </option>
      ))}
    </select>
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
