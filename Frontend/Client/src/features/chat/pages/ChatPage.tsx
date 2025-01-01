import ChatList from "../components/ChatList";
import ChatWindow from "../components/ChatWindow";

const ChatPage: React.FC = () => {
  return (
    <div className="flex h-screen bg-gray-100 text-gray-900 dark:bg-gray-900 dark:text-gray-100">
      {/* Chat List */}
      <div className="w-1/4 bg-gray-200 border-r border-gray-300 dark:bg-gray-800 dark:border-gray-700">
        <ChatList />
      </div>

      {/* Chat Window */}
      <div className="flex-grow bg-gray-50 dark:bg-gray-900">
        <ChatWindow />
      </div>
    </div>
  );
};

export default ChatPage;
