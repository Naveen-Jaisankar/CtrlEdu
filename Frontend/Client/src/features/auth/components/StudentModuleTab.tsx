import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

interface Module {
    moduleId: number;
    moduleCode: string;
    moduleName: string;
    teacherName: string;
}

interface Class {
    classId: number;
    className: string;
    moduleNames: string[];
    studentNames: string[];
    moduleIds: number[];
}

const StudentModuleTab: React.FC = () => {
    const [studentModules, setStudentModules] = useState<Module[]>([]);
    const [loading, setLoading] = useState(false);

    // Decode student name from token
    const getStudentNameFromToken = (): string | null => {
        const token = localStorage.getItem("accessToken");
        if (token) {
            try {
                const decodedToken = jwtDecode<any>(token);
                const fullName = `${decodedToken.given_name} ${decodedToken.family_name}`;
                console.log("Decoded Student Name:", fullName);
                return fullName;
            } catch (error) {
                console.error("Error decoding token:", error);
            }
        }
        return null;
    };

    const loggedInStudentName = getStudentNameFromToken();

    useEffect(() => {
        fetchStudentModules();
    }, []);

    const fetchStudentModules = async () => {
        setLoading(true);
        try {
            // Fetch all classes and modules
            const [classResponse, moduleResponse] = await Promise.all([
                axios.get("http://localhost:8084/api/admin/classes", {
                    headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` }
                }),
                axios.get("http://localhost:8084/api/admin/modules", {
                    headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` }
                })
            ]);

            // Filter classes where the logged-in student is present
            const studentClasses = classResponse.data.filter((classItem: Class) =>
                classItem.studentNames.includes(loggedInStudentName)
            );

            // Extract all module IDs for those classes
            const studentModuleIds = studentClasses.flatMap(classItem => classItem.moduleIds);

            // Filter modules matching those IDs
            const filteredModules = moduleResponse.data.filter((module: Module) =>
                studentModuleIds.includes(module.moduleId)
            );

            setStudentModules(filteredModules);
        } catch (error) {
            console.error("Error fetching modules:", error);
            alert("Failed to fetch student modules");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="text-white p-6">
            <h1 className="text-2xl font-bold mb-6">My Modules</h1>
            {loading ? (
                <p>Loading...</p>
            ) : (
                <table className="table-auto w-full border-collapse border border-gray-700">
                    <thead>
                        <tr className="bg-neutral-800">
                            <th className="border border-gray-600 px-4 py-2">Module Code</th>
                            <th className="border border-gray-600 px-4 py-2">Module Name</th>
                            <th className="border border-gray-600 px-4 py-2">Teacher</th>
                        </tr>
                    </thead>
                    <tbody>
                        {studentModules.length > 0 ? (
                            studentModules.map((module) => (
                                <tr key={module.moduleId}>
                                    <td className="border border-gray-600 px-4 py-2">{module.moduleCode}</td>
                                    <td className="border border-gray-600 px-4 py-2">{module.moduleName}</td>
                                    <td className="border border-gray-600 px-4 py-2">{module.teacherName}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={3} className="text-center border border-gray-600 px-4 py-2">
                                    No modules found for this student.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default StudentModuleTab;
