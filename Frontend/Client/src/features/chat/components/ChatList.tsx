import React from "react";

type ChatListProps = {
  onSelectTopic: (topic: string) => void;
};

const ChatList: React.FC<ChatListProps> = ({ onSelectTopic }) => {
  const chats = [
    {
      name: "Odama Studio",
      lastMessage: "Mas Happy Typing...",
      topic: "odama",
    },
    { name: "Hatypo Studio", lastMessage: "Lah gas!", topic: "hatypo" },
    { name: "Nolaaa", lastMessage: "Keren banget!", topic: "nolaaa" },
  ];

  return (
    <div className="h-full bg-gray-200 dark:bg-gray-800">
      <div className="py-4 px-6 border-b border-gray-300 dark:border-gray-700">
        <h2 className="text-lg font-semibold">Messages</h2>
      </div>
      <ul>
        {chats.map((chat, index) => (
          <li
            key={index}
            onClick={() => onSelectTopic(chat.topic)}
            className="flex items-center py-4 px-6 hover:bg-gray-100 dark:hover:bg-gray-700 cursor-pointer"
          >
            <div className="w-12 h-12 bg-gray-400 dark:bg-gray-600 rounded-full mr-4"></div>
            <div>
              <p className="font-semibold text-gray-900 dark:text-gray-100">
                {chat.name}
              </p>
              <p className="text-sm text-gray-500 dark:text-gray-400 truncate">
                {chat.lastMessage}
              </p>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ChatList;
