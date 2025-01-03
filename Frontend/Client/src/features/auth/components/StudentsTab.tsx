/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

interface Class {
  classId: number;
  className: string;
  numStudents: number;
  moduleNames: string[];
  studentNames: string[];
  moduleIds: number[];
}

interface Module {
  moduleId: number;
  moduleName: string;
  teacherName: string;
  teacherId: number;
}

const StudentTab: React.FC = () => {
  const [classes, setClasses] = useState<Class[]>([]);
  const [loading, setLoading] = useState(false);

  // Decode teacher name from the token
  const getTeacherNameFromToken = (): string | null => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      try {
        const decodedToken = jwtDecode<any>(token);
        const fullName = `${decodedToken.given_name} ${decodedToken.family_name}`;
        return fullName;
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
    return null;
  };

  const loggedInTeacherName = getTeacherNameFromToken();

  useEffect(() => {
    fetchClassData();
  }, []);

  const fetchClassData = async () => {
    setLoading(true);
    try {
      // Fetch both classes and modules
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

      // Filter modules where the teacher is assigned
      const teacherModules = moduleResponse.data.filter(
        (module: Module) => module.teacherName === loggedInTeacherName
      );

      // Extract the module IDs associated with this teacher
      const teacherModuleIds = teacherModules.map((module) => module.moduleId);

      // Filter classes where at least one module matches
      const filteredClasses = classResponse.data.filter((classItem: Class) =>
        classItem.moduleIds.some((moduleId) =>
          teacherModuleIds.includes(moduleId)
        )
      );

      setClasses(filteredClasses);
    } catch (error) {
      console.error("Error fetching class data:", error);
      alert("Failed to fetch classes");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="text-white p-6">
      <h1 className="text-2xl font-bold mb-6">Class and Student Information</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="table-auto w-full border-collapse border border-gray-700">
          <thead>
            <tr className="bg-neutral-800">
              <th className="border border-gray-600 px-4 py-2">Class Name</th>
              <th className="border border-gray-600 px-4 py-2">
                Number of Students
              </th>
              <th className="border border-gray-600 px-4 py-2">Modules</th>
              <th className="border border-gray-600 px-4 py-2">Students</th>
            </tr>
          </thead>
          <tbody>
            {classes.length > 0 ? (
              classes.map((classItem) => (
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
                  <td className="border border-gray-600 px-4 py-2">
                    {classItem.studentNames.length > 0
                      ? classItem.studentNames.join(", ")
                      : "No students assigned"}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan={4}
                  className="text-center border border-gray-600 px-4 py-2"
                >
                  No classes or students found for this teacher.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default StudentTab;
