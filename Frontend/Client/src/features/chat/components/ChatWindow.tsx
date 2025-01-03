/* eslint-disable @typescript-eslint/no-unused-vars */

import React, { useState, useEffect, useCallback } from "react";
import ChatHeader from "./ChatHeader";
import MessageList from "./MessageList";
import ChatInput from "./ChatInput";
import useWebSocket from "../hooks/useWebSocket";
import { sendMessage, getMessages } from "../services/chatService";
import { Message } from "../types/Message";
import { formatTime } from "../utils/common";

const ChatWindow: React.FC<{ topic: string; userid: string }> = ({
  topic,
  userid,
}) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  // Handle new messages received via WebSocket
  const handleWebSocketMessage = useCallback((newMessage: Message) => {
    setMessages((prevMessages) => [
      ...prevMessages,
      { ...newMessage, time: formatTime(newMessage.timestamp) },
    ]);
  }, []);

  // Initialize WebSocket
  const { sendMessage: sendWebSocketMessage } = useWebSocket<Message>(
    `ws://localhost:8085/ws-chat`,
    handleWebSocketMessage
  );

  // Fetch messages when the topic changes
  useEffect(() => {
    const fetchMessages = async () => {
      if (!topic) return;

      setLoading(true);
      try {
        const fetchedMessages = await getMessages(topic, 20); // Fetch the last 20 messages
        const formattedMessages = fetchedMessages.map((msg) => ({
          ...msg,
          time: formatTime(msg.timestamp),
        }));
        setMessages(formattedMessages);
      } catch (error) {
        console.error("Error fetching messages:", error);
        alert("Failed to load messages. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchMessages();
  }, [topic]);

  // Handle sending a message
  const handleSendMessage = async (content: string) => {
    const [_, classId, __, moduleId] = topic.split("_");

    const message: Message = {
      sender: userid,
      content,
      topic,
      timestamp: Date.now(),
      time: "",
      classId,
      moduleId,
    };

    try {
      await sendMessage(message); // Send to backend
      sendWebSocketMessage(message); // Broadcast message via WebSocket
      setMessages((prevMessages) => [
        ...prevMessages,
        { ...message, time: formatTime(message.timestamp) },
      ]);
    } catch (error) {
      console.error("Error sending message:", error);
      alert("Failed to send message. Please try again.");
    }
  };

  return (
    <div className="flex flex-col flex-grow bg-gray-50 dark:bg-gray-900 h-full">
      <ChatHeader />
      {loading ? (
        <div className="flex items-center justify-center flex-grow">
          <p>Loading messages...</p>
        </div>
      ) : (
        <MessageList messages={messages} />
      )}
      <ChatInput onSendMessage={handleSendMessage} />
    </div>
  );
};

export default ChatWindow;
