import React from "react";
import ChatWindow from "../components/ChatWindow";
import ChatList from "../components/ChatList";

const ChatPage: React.FC = () => {
  return (
    <div className="flex h-screen bg-gray-900 text-gray-100">
      {/* Chat List */}
      <div className="w-1/4 bg-gray-800 border-r border-gray-700">
        <ChatList />
      </div>

      {/* Chat Window */}
      <div className="flex-grow bg-gray-900">
        <ChatWindow />
      </div>
    </div>
  );
};

export default ChatPage;
