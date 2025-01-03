import { useRef } from "react";

type Message = {
  sender: string;
  content: string;
  time: string;
};

type MessageListProps = {
  messages: Message[];
};

const MessageList: React.FC<MessageListProps> = ({ messages }) => {
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const loggedInUserId = localStorage.getItem("userId") || ""; // Adjust based on how you store the user ID

  return (
    <div className="flex-grow overflow-y-auto p-6 space-y-4 bg-gray-50 dark:bg-gray-900">
      {messages.map((msg, index) => (
        <div
          key={index}
          className={`flex ${
            msg.sender === loggedInUserId ? "justify-end" : "justify-start"
          }`}
        >
          <div
            className={`max-w-xs px-4 py-2 rounded-lg ${
              msg.sender === loggedInUserId
                ? "bg-blue-500 text-white"
                : "bg-gray-200 text-gray-900 dark:bg-gray-700 dark:text-gray-100"
            }`}
          >
            <p>{msg.content}</p>
            <p className="text-xs text-gray-400 dark:text-gray-500 mt-1">
              {msg.time}
            </p>
          </div>
        </div>
      ))}

      <div ref={messagesEndRef}></div>
    </div>
  );
};

export default MessageList;
