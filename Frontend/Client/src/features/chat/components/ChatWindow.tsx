import React, { useState, useEffect, useCallback } from "react";
import ChatHeader from "./ChatHeader";
import MessageList from "./MessageList";
import ChatInput from "./ChatInput";
import useWebSocket from "../hooks/useWebSocket";
import { sendMessage, getMessages } from "../services/chatService";
import { Message } from "../types/Message";

const ChatWindow: React.FC<{ topic: string }> = ({ topic }) => {
  const [messages, setMessages] = useState<Message[]>([]);

  const formatTime = (timestamp: number): string => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  };

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

  // Fetch previous messages when component mounts
  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const fetchedMessages = await getMessages(topic);

        // Add the `time` property to each message
        const formattedMessages = fetchedMessages.map((msg) => ({
          ...msg,
          time: formatTime(msg.timestamp),
        }));

        setMessages(formattedMessages);
      } catch (error) {
        console.error("Error fetching messages:", error);
      }
    };
    fetchMessages();
  }, [topic]);

  // Handle message send
  const handleSendMessage = async (content: string) => {
    const message: Message = {
      sender: "You",
      content,
      topic,
      timestamp: Date.now(),
    };

    try {
      // Send message to the backend (via REST API)
      await sendMessage(message);

      // Send message via WebSocket
      sendWebSocketMessage(message);

      // Add the message locally for instant feedback
      setMessages((prevMessages) => [
        ...prevMessages,
        { ...message, time: formatTime(message.timestamp) },
      ]);
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  return (
    <div className="flex flex-col flex-grow bg-gray-50 dark:bg-gray-900 h-full">
      <ChatHeader />
      <MessageList messages={messages} />
      <ChatInput onSendMessage={handleSendMessage} />
    </div>
  );
};

export default ChatWindow;
