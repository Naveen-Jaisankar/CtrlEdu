import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

const useWebSocket = (
  topic: string,
  onMessageReceived: (message: any) => void
) => {
  const [client, setClient] = useState<Client | null>(null);
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: "ws://localhost:8085/ws-chat",
      debug: (str) => console.log("STOMP Debug:", str),
      reconnectDelay: 5000,
      onConnect: () => {
        setConnected(true);
        console.log(`✅ Connected and subscribing to: /topic/${topic}`);
        stompClient.subscribe(`/topic/${topic}`, (message) => {
          const parsedMessage = JSON.parse(message.body);
          onMessageReceived(parsedMessage);
        });
      },
      onStompError: (error) => {
        console.error("❌ STOMP Error:", error);
      },
    });

    stompClient.activate();
    setClient(stompClient);

    return () => {
      stompClient.deactivate();
    };
  }, [topic, onMessageReceived]);

  const sendMessage = (message: any) => {
    if (client && connected) {
      console.log("📤 Sending message:", message);
      client.publish({
        destination: `/app/send`,
        body: JSON.stringify(message),
      });
    } else {
      console.error("❌ WebSocket not connected!");
    }
  };

  return { sendMessage, connected };
};

export default useWebSocket;
