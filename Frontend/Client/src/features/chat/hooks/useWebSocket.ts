import { useEffect, useRef, useCallback } from "react";

const useWebSocket = <T>(url: string, onMessage: (message: T) => void) => {
  const socket = useRef<WebSocket | null>(null);

  const sendMessage = useCallback((message: T) => {
    if (socket.current && socket.current.readyState === WebSocket.OPEN) {
      socket.current.send(JSON.stringify(message));
    } else {
      console.error("WebSocket is not open");
    }
  }, []);

  useEffect(() => {
    socket.current = new WebSocket(url);

    socket.current.onopen = () => {
      console.log("WebSocket connection established");
    };

    socket.current.onmessage = (event) => {
      try {
        const data: T = JSON.parse(event.data);
        onMessage(data);
      } catch (err) {
        console.error("Error parsing WebSocket message:", err);
      }
    };

    socket.current.onclose = () => {
      console.log("WebSocket connection closed");
    };

    socket.current.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    return () => {
      if (socket.current) {
        console.log("Closing WebSocket connection");
        socket.current.close();
      }
    };
  }, [url, onMessage]);

  return { sendMessage };
};

export default useWebSocket;
