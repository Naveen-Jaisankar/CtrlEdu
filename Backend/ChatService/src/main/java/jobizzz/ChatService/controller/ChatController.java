package jobizzz.ChatService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jobizzz.ChatService.model.ChatMessage;
import jobizzz.ChatService.service.MessageService;
import jobizzz.ChatService.service.PubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Tag(name = "Chat", description = "Chat Management API for sending messages and fetching chat history")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private PubSubService pubSubService;

    @Operation(summary = "Send a message to a specific chat room via REST", description = "Publishes a message to a specified chat room and stores it in Redis.")
    @PostMapping("/send/{roomId}")
    public void sendMessage(
            @Parameter(description = "ID of the chat room", required = true) @PathVariable String roomId,
            @RequestBody ChatMessage message) {
        message.setRoomId(roomId);
        messageService.sendMessage(roomId, message);
        pubSubService.publishMessage("RoomMessages:" + roomId, message);
    }

    @Operation(summary = "Get chat history for a specific room", description = "Retrieves the message history for a specified chat room.")
    @GetMapping("/history/{roomId}")
    public List<Object> getChatHistory(
            @Parameter(description = "ID of the chat room", required = true) @PathVariable String roomId) {
        return messageService.getChatHistory(roomId);
    }

    // WebSocket endpoint for real-time messaging
    @MessageMapping("/sendMessage")
    public void sendWebSocketMessage(ChatMessage message) {
        // Reuse sendMessage logic to handle both WebSocket and REST requests
        sendMessage(message.getRoomId(), message);
    }
}
