import { useState } from "react";

type ChatInputProps = {
  onSendMessage: (message: string) => void;
};

const ChatInput: React.FC<ChatInputProps> = ({ onSendMessage }) => {
  const [newMessage, setNewMessage] = useState("");

  const handleSend = () => {
    if (newMessage.trim() !== "") {
      onSendMessage(newMessage);
      setNewMessage("");
    }
  };

  return (
    <div className="p-4 border-t border-gray-700 bg-gray-800 flex items-center">
      <input
        type="text"
        className="flex-grow px-4 py-2 border rounded-md bg-gray-700 text-gray-100 focus:outline-none focus:ring-2 focus:ring-teal-500"
        placeholder="Type a message..."
        value={newMessage}
        onChange={(e) => setNewMessage(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") {
            handleSend();
          }
        }}
      />
      <button
        onClick={handleSend}
        className="ml-4 px-4 py-2 bg-teal-500 text-white rounded-md hover:bg-teal-600 transition"
      >
        Send
      </button>
    </div>
  );
};

export default ChatInput;
