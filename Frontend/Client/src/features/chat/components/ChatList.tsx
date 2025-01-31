import React from "react";
import useChatGroups from "../hooks/useChatGroups";

type ChatListProps = {
  onSelectTopic: (topic: string, userid: string) => void;
};

const ChatList: React.FC<ChatListProps> = ({ onSelectTopic }) => {
  const { chatGroups, loading, error } = useChatGroups();

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p className="text-red-500">{error}</p>;
  }

  if (chatGroups.length === 0) {
    return <p>No chat groups available.</p>;
  }

  return (
    <div className="h-full bg-gray-200 dark:bg-gray-800">
      <div className="py-4 px-6 border-b border-gray-300 dark:border-gray-700">
        <h2 className="font-semibold text-lg text-gray-900 dark:text-gray-100">
          Chat Groups
        </h2>
      </div>
      <ul>
        {chatGroups.map((group) => (
          <li
            key={`${group.classId}-${group.moduleId}`}
            onClick={() =>
              onSelectTopic(
                `class_${group.classId}_module_${group.moduleId}`,
                `${group.userID}`
              )
            }
            className="flex items-center py-4 px-6 hover:bg-gray-100 dark:hover:bg-gray-700 cursor-pointer"
          >
            <div className="w-12 h-12 bg-gray-400 dark:bg-gray-600 rounded-full mr-4 flex items-center justify-center">
              <span className="text-white font-semibold">
                {group.className[0]}
              </span>
            </div>
            <div>
              <p className="font-semibold text-gray-900 dark:text-gray-100">
                {group.className}
              </p>
              <p className="text-sm text-gray-500 dark:text-gray-400 truncate">
                {group.moduleName}
              </p>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ChatList;
