/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState, useEffect } from "react";
import axios from "axios";

export type ChatGroup = {
  userId: number;
  userName: string;
  classId: number;
  className: string;
  moduleId: number;
  moduleName: string;
};

type UseChatGroupsResult = {
  chatGroups: ChatGroup[];
  loading: boolean;
  error: string | null;
};

const useChatGroups = (): UseChatGroupsResult => {
  const [chatGroups, setChatGroups] = useState<ChatGroup[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchChatGroups = async () => {
      try {
        const response = await axios.get<ChatGroup[]>(
          "http://localhost:8084/api/client/contacts",
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
          }
        );
        setChatGroups(response.data);
      } catch (err: any) {
        console.error("Error fetching chat groups:", err);
        setError(err.message || "Failed to load chat groups.");
      } finally {
        setLoading(false);
      }
    };

    fetchChatGroups();
  }, []);

  return { chatGroups, loading, error };
};

export default useChatGroups;
