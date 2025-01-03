// import React, { useState } from "react";
// import ChatWindow from "../components/ChatWindow";
// import ChatList from "../components/ChatList";

// const ChatPage: React.FC = () => {
//   const [selectedTopic, setSelectedTopic] = useState<string>(""); // Selected chat topic
//   const { contacts, groups, error } = useContacts();

//   return (
//     <div className="flex flex-col h-full">
//       {/* Header */}
//       <div className="p-4 bg-gray-800 text-white text-lg font-semibold">
//         Messages
//       </div>

//       {/* Main Content */}
//       <div className="flex flex-grow bg-gray-100 dark:bg-gray-800">
//         {/* Chat List */}
//         <div className="w-1/4 bg-gray-200 dark:bg-gray-700 border-r border-gray-300 dark:border-gray-600">
//           {/* {error && <p className="text-red-500 p-4">{error}</p>} */}
//           <ChatList
//             chats={[
//               ...contacts.map((contact) => ({
//                 name: contact.name,
//                 lastMessage: contact.lastMessage,
//                 topic: `contact_${contact.id}`,
//               })),
//               ...groups.map((group) => ({
//                 name: group.name,
//                 lastMessage: group.lastMessage,
//                 topic: `group_${group.id}`,
//               })),
//             ]}
//             onSelectTopic={setSelectedTopic}
//           />
//         </div>

//         {/* Chat Window */}
//         <div className="flex-grow">
//           {selectedTopic ? (
//             <ChatWindow topic={selectedTopic} />
//           ) : (
//             <div className="flex items-center justify-center h-full text-gray-500">
//               Select a contact or group to start chatting
//             </div>
//           )}
//         </div>
//       </div>
//     </div>
//   );
// };

// export default ChatPage;

import React, { useState } from "react";
import ChatWindow from "../components/ChatWindow";
import ChatList from "../components/ChatList";

const ChatPage: React.FC = () => {
  const [selectedTopic, setSelectedTopic] = useState<string>("odama"); // Default topic

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
          <ChatList onSelectTopic={setSelectedTopic} />
        </div>

        {/* Chat Window */}
        <div className="flex-grow">
          <ChatWindow topic={selectedTopic} />
        </div>
      </div>
    </div>
  );
};

export default ChatPage;
