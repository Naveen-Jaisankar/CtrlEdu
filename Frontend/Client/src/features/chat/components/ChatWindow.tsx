import React, { useState } from "react";
import ChatHeader from "./ChatHeader";
import MessageList from "./MessageList";
import ChatInput from "./ChatInput";

const ChatWindow: React.FC = () => {
  const [messages, setMessages] = useState([
    { sender: "Mas Happy", content: "Hey, how are you?", time: "5:00 PM" },
    { sender: "You", content: "I'm good, thanks!", time: "5:02 PM" },
    { sender: "Mas Happy", content: "Glad to hear!", time: "5:05 PM" },
  ]);

  const handleSendMessage = (newMessage: string) => {
    if (newMessage.trim() !== "") {
      setMessages((prevMessages) => [
        ...prevMessages,
        {
          sender: "You",
          content: newMessage,
          time: new Date().toLocaleTimeString(),
        },
      ]);
    }
  };

  return (
    <div className="flex flex-col h-full">
      <ChatHeader />
      <MessageList messages={messages} />
      <ChatInput onSendMessage={handleSendMessage} />
    </div>
  );
};

export default ChatWindow;
