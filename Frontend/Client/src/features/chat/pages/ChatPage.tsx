import React, { useState } from "react";
import ChatWindow from "../components/ChatWindow";
import ChatList from "../components/ChatList";

const ChatPage: React.FC = () => {
  const [selectedTopic, setSelectedTopic] = useState<string>("");
  const [selectedUserId, setSelectedUserId] = useState<string>("");

  const handleSelectChat = (topic: string, userId: string) => {
    setSelectedTopic(topic);
    setSelectedUserId(userId);
    console.log(userId);
    localStorage.setItem("userId", userId);
  };

  return (
    <div className="flex flex-col h-full">
      {/* Page Header */}
      <div className="p-4 bg-gray-800 text-white text-lg font-semibold">
        Messages
      </div>

      {/* Chat Section */}
      <div className="flex flex-grow bg-gray-100 dark:bg-gray-800">
        {/* Chat List */}
        <div className="w-1/4 bg-gray-200 dark:bg-gray-700 border-r border-gray-300 dark:border-gray-600">
          <ChatList onSelectTopic={handleSelectChat} />
        </div>

        {/* Chat Window */}
        <div className="flex-grow">
          {selectedTopic && selectedUserId !== "" ? (
            <ChatWindow topic={selectedTopic} userid={selectedUserId} />
          ) : (
            <div className="flex items-center justify-center h-full text-gray-500">
              Select a chat group to start chatting
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ChatPage;
