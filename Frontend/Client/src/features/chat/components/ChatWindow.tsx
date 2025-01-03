// /* eslint-disable @typescript-eslint/no-unused-vars */
// // /* eslint-disable @typescript-eslint/no-unused-vars */

// import React, { useState, useEffect, useCallback } from "react";
// import ChatHeader from "./ChatHeader";
// import MessageList from "./MessageList";
// import ChatInput from "./ChatInput";
// import useWebSocket from "../hooks/useWebSocket";
// import { getMessages, persistMessage } from "../services/chatService";
// import { Message } from "../types/Message";
// import { formatTime } from "../utils/common";

// const ChatWindow: React.FC<{ topic: string; userid: string }> = ({
//   topic,
//   userid,
// }) => {
//   const [messages, setMessages] = useState<Message[]>([]);

//   const handleWebSocketMessage = useCallback((newMessage: Message) => {
//     console.log("📥 Message received:", newMessage);
//     setMessages((prevMessages) => [
//       ...prevMessages,
//       { ...newMessage, time: formatTime(newMessage.timestamp) },
//     ]);
//   }, []);

//   // Initialize WebSocket inside the functional component (fixing the error)
//   const { sendMessage } = useWebSocket(topic, handleWebSocketMessage);

//   useEffect(() => {
//     const fetchMessages = async () => {
//       try {
//         const historicalMessages = await getMessages(topic);
//         setMessages(historicalMessages);
//       } catch (error) {
//         console.error("Failed to fetch messages:", error);
//       }
//     };
//     fetchMessages();
//   }, [topic]);

//   // Handle both WebSocket and REST persistence
//   const handleSendMessage = async (content: string) => {
//     const [_, classId, __, moduleId] = topic.split("_");
//     if (!content.trim()) return;

//     const message: Message = {
//       sender: userid,
//       content,
//       topic,
//       timestamp: Date.now(),
//       time: "",
//       classId,
//       moduleId,
//     };

//     try {
//       // Send via WebSocket for real-time
//       sendMessage(message);

//       // Persist the message in the database via REST
//       await persistMessage(message);

//       console.log("✅ Message sent and persisted successfully.");
//     } catch (error) {
//       console.error("Failed to send message:", error);
//     }
//   };

//   return (
//     <div className="flex flex-col flex-grow bg-gray-50 dark:bg-gray-900 h-full">
//       <ChatHeader />
//       {messages.length === 0 ? (
//         <div className="flex items-center justify-center flex-grow">
//           <p>No messages yet. Start a conversation!</p>
//         </div>
//       ) : (
//         <MessageList messages={messages} />
//       )}
//       <ChatInput onSendMessage={handleSendMessage} />
//     </div>
//   );
// };

// export default ChatWindow;
import React, { useState, useEffect, useCallback } from "react";
import ChatHeader from "./ChatHeader";
import MessageList from "./MessageList";
import ChatInput from "./ChatInput";
import useWebSocket from "../hooks/useWebSocket";
import { getMessages, persistMessage } from "../services/chatService";
import { Message } from "../types/Message";
import { formatTime } from "../utils/common";

const ChatWindow: React.FC<{ topic: string; userid: string }> = ({
  topic,
  userid,
}) => {
  const [messages, setMessages] = useState<Message[]>([]);

  // WebSocket message handler (State only updated when received from server)
  const handleWebSocketMessage = useCallback((newMessage: Message) => {
    console.log("📥 Message received from WebSocket:", newMessage);
    setMessages((prevMessages) => [...prevMessages, { ...newMessage }]);
  }, []);

  // WebSocket connection initialized once
  const { sendMessage } = useWebSocket(topic, handleWebSocketMessage);

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const historicalMessages = await getMessages(topic);
        setMessages(historicalMessages);
      } catch (error) {
        console.error("Failed to fetch messages:", error);
      }
    };
    fetchMessages();
  }, [topic]);

  // Send message via WebSocket and persist via REST
  const handleSendMessage = async (content: string) => {
    if (!content.trim()) return;

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
      // Send via WebSocket only (State update happens on receiving)
      sendMessage(message);

      // Persist the message using REST API
      await persistMessage(message);

      console.log("✅ Message sent and persisted successfully.");
    } catch (error) {
      console.error("Failed to send message:", error);
    }
  };

  return (
    <div className="flex flex-col flex-grow bg-gray-50 dark:bg-gray-900 h-full">
      <ChatHeader />
      {messages.length === 0 ? (
        <div className="flex items-center justify-center flex-grow">
          <p>No messages yet. Start a conversation!</p>
        </div>
      ) : (
        <MessageList messages={messages} />
      )}
      <ChatInput onSendMessage={handleSendMessage} />
    </div>
  );
};

export default ChatWindow;
