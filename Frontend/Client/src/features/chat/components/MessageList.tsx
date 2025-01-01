type Message = {
  sender: string;
  content: string;
  time: string;
};

type MessageListProps = {
  messages: Message[];
};

const MessageList: React.FC<MessageListProps> = ({ messages }) => {
  return (
    <div className="flex-grow overflow-y-auto p-6 space-y-4 bg-gray-900">
      {messages.map((msg, index) => (
        <div
          key={index}
          className={`flex ${
            msg.sender === "You" ? "justify-end" : "justify-start"
          }`}
        >
          <div
            className={`max-w-xs px-4 py-2 rounded-lg ${
              msg.sender === "You"
                ? "bg-teal-500 text-white"
                : "bg-gray-700 text-gray-200"
            }`}
          >
            <p>{msg.content}</p>
            <p className="text-xs text-gray-400 mt-1">{msg.time}</p>
          </div>
        </div>
      ))}
    </div>
  );
};

export default MessageList;
