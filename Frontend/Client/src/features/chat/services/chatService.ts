import axios from "axios";
import { Message } from "../types/Message";

const apiClient = axios.create({
  baseURL: "http://localhost:8084/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Fetch messages from Redis via REST API
export const getMessages = async (
  topic: string,
  offset: number = 0
): Promise<Message[]> => {
  try {
    const response = await apiClient.get<{ messages: Message[] }>("/message", {
      params: { topic, offset },
    });
    return response.data.messages;
  } catch (error) {
    console.error("Error fetching messages:", error);
    throw error;
  }
};

// Persist message to the backend using REST API only
export const persistMessage = async (message: Message): Promise<void> => {
  try {
    await apiClient.post("/message", message);
    console.log("💾 Message persisted via REST API:", message);
  } catch (error) {
    console.error("Error persisting message:", error);
    throw error;
  }
};
