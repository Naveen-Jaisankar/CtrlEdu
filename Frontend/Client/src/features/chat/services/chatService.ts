// import axios from "axios";
// import { Message } from "../types/Message";

// const apiClient = axios.create({
//   baseURL: "http://localhost:8084/api",
//   headers: {
//     "Content-Type": "application/json",
//   },
// });

// // Interceptors for debugging
// apiClient.interceptors.request.use((request) => {
//   console.log("Starting Request:", request);
//   return request;
// });

// apiClient.interceptors.response.use(
//   (response) => response,
//   (error) => {
//     console.error("Error Response:", error.response || error.message);
//     return Promise.reject(error);
//   }
// );

// export const sendMessage = async (message: Message): Promise<void> => {
//   await apiClient.post("/message", message);
// };

// export const getMessages = async (
//   topic: string,
//   offset: number = 0
// ): Promise<Message[]> => {
//   const response = await apiClient.get<{ messages: Message[] }>("/message", {
//     params: { topic, offset },
//   });
//   console.log(response);
//   return response.data.messages;
// };

import axios from "axios";
import { Message } from "../types/Message";

const apiClient = axios.create({
  baseURL: "http://localhost:8084/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Fetch messages for a given topic
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

// Send a message to the backend
export const sendMessage = async (message: Message): Promise<void> => {
  await apiClient.post("/message", message);
};
