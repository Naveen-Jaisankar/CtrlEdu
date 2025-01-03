/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

interface Module {
  moduleId: number;
  moduleCode: string;
  moduleName: string;
  teacherName: string;
  teacherId?: number;
}

interface Class {
  classId: number;
  className: string;
  numStudents: number;
  moduleIds: number[];
  studentNames: string[];
  moduleNames: string[];
}

const TeacherModuleTab: React.FC = () => {
  const [assignedClasses, setAssignedClasses] = useState<Class[]>([]);
  const [assignedModules, setAssignedModules] = useState<Module[]>([]);
  const [loading, setLoading] = useState(false);

  const getTeacherNameFromToken = (): string | null => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      try {
        const decodedToken = jwtDecode<any>(token);
        const fullName = `${decodedToken.given_name} ${decodedToken.family_name}`;
        console.log("Decoded Teacher Name:", fullName);
        return fullName;
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
    return null;
  };

  const loggedInTeacherName = getTeacherNameFromToken();

  useEffect(() => {
    if (loggedInTeacherName) {
      fetchTeacherData();
    } else {
      alert("Teacher name not found in token. Please log in again.");
    }
  }, []);

  const fetchTeacherData = async () => {
    setLoading(true);
    try {
      const [classResponse, moduleResponse] = await Promise.all([
        axios.get("http://localhost:8084/api/admin/classes", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }),
        axios.get("http://localhost:8084/api/admin/modules", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }),
      ]);

      // Filter modules and classes based on teacher name
      const filteredModules = moduleResponse.data.filter(
        (module: Module) => module.teacherName === loggedInTeacherName
      );

      const filteredClasses = classResponse.data.filter((classItem: Class) =>
        classItem.moduleIds.some((moduleId) =>
          filteredModules.some((module) => module.moduleId === moduleId)
        )
      );

      setAssignedModules(filteredModules);
      setAssignedClasses(filteredClasses);
    } catch (error) {
      console.error("Error fetching teacher data:", error);
      alert("Failed to load teacher data.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="text-white p-6">
      <h1 className="text-2xl font-bold mb-6">Assigned Classes and Modules</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          {/* Display Assigned Modules */}
          <h2 className="text-xl font-semibold mt-4">Modules</h2>
          <table className="table-auto w-full border-collapse border border-gray-700 mb-6">
            <thead>
              <tr className="bg-neutral-800">
                <th className="border border-gray-600 px-4 py-2">
                  Module Code
                </th>
                <th className="border border-gray-600 px-4 py-2">
                  Module Name
                </th>
              </tr>
            </thead>
            <tbody>
              {assignedModules.length > 0 ? (
                assignedModules.map((module) => (
                  <tr key={module.moduleId}>
                    <td className="border border-gray-600 px-4 py-2">
                      {module.moduleCode}
                    </td>
                    <td className="border border-gray-600 px-4 py-2">
                      {module.moduleName}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan={2}
                    className="text-center border border-gray-600 px-4 py-2"
                  >
                    No modules assigned.
                  </td>
                </tr>
              )}
            </tbody>
          </table>

          {/* Display Assigned Classes */}
          <h2 className="text-xl font-semibold mt-4">Classes</h2>
          <table className="table-auto w-full border-collapse border border-gray-700">
            <thead>
              <tr className="bg-neutral-800">
                <th className="border border-gray-600 px-4 py-2">Class Name</th>
                <th className="border border-gray-600 px-4 py-2">
                  Number of Students
                </th>
                <th className="border border-gray-600 px-4 py-2">
                  Module Names
                </th>
              </tr>
            </thead>
            <tbody>
              {assignedClasses.length > 0 ? (
                assignedClasses.map((classItem) => (
                  <tr key={classItem.classId}>
                    <td className="border border-gray-600 px-4 py-2">
                      {classItem.className}
                    </td>
                    <td className="border border-gray-600 px-4 py-2">
                      {classItem.numStudents}
                    </td>
                    <td className="border border-gray-600 px-4 py-2">
                      {classItem.moduleNames.join(", ")}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan={3}
                    className="text-center border border-gray-600 px-4 py-2"
                  >
                    No classes assigned.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
};

export default TeacherModuleTab;
