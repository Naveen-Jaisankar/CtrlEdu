import React, { useState } from "react";
import ChatWindow from "../components/ChatWindow";
import ChatList from "../components/ChatList";

const ChatPage: React.FC = () => {
  const [selectedTopic, setSelectedTopic] = useState<string>("odama"); // Default topic

  return (
    <div className="flex h-screen bg-gray-100 dark:bg-gray-800">
      {/* Chat List */}
      <div className="w-1/4">
        <ChatList onSelectTopic={setSelectedTopic} />
      </div>

      {/* Chat Window */}
      <div className="flex-grow">
        <ChatWindow topic={selectedTopic} />
      </div>
    </div>
  );
};

export default ChatPage;
