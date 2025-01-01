const ChatList: React.FC = () => {
  const chats = [
    {
      name: "Odama Studio",
      lastMessage: "Mas Happy Typing...",
      time: "5:11 PM",
    },
    { name: "Hatypo Studio", lastMessage: "Lah gas!", time: "4:01 PM" },
    { name: "Nolaaa", lastMessage: "Keren banget!", time: "3:29 PM" },
  ];

  return (
    <div className="h-full">
      <div className="py-4 px-6 border-b border-gray-300 dark:border-gray-700">
        <h2 className="text-lg font-semibold text-gray-900 dark:text-gray-200">
          Messages
        </h2>
      </div>
      <ul>
        {chats.map((chat, index) => (
          <li
            key={index}
            className={`flex items-center py-4 px-6 hover:bg-gray-100 dark:hover:bg-gray-700 cursor-pointer ${
              index === 0 ? "bg-gray-100 dark:bg-gray-700" : ""
            }`}
          >
            <div className="w-12 h-12 bg-gray-400 dark:bg-gray-600 rounded-full mr-4"></div>
            <div className="flex-1">
              <p className="font-semibold text-gray-900 dark:text-gray-100">
                {chat.name}
              </p>
              <p className="text-sm text-gray-500 dark:text-gray-400 truncate">
                {chat.lastMessage}
              </p>
            </div>
            <div className="text-xs text-gray-400 dark:text-gray-500">
              {chat.time}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ChatList;
