import axios from "axios";

export type ChatGroup = {
  userId: number;
  userName: string;
  classId: number;
  className: string;
  moduleId: number;
  moduleName: string;
};

// Create an Axios instance for shared configurations
const apiClient = axios.create({
  baseURL: "/api/client",
  headers: {
    "Content-Type": "application/json",
  },
});

// Fetch chat groups for a student
export const fetchChatGroups = async (): Promise<ChatGroup[]> => {
  const accessToken = localStorage.getItem("accessToken");
  if (!accessToken) {
    throw new Error("Access token is missing. Please log in again.");
  }

  try {
    const response = await apiClient.get<ChatGroup[]>("/contacts", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching chat groups:", error);

    // Customize error message for specific cases
    if (axios.isAxiosError(error)) {
      const message =
        error.response?.data?.message || "Failed to fetch chat groups.";
      throw new Error(message);
    }

    throw new Error("An unexpected error occurred.");
  }
};
