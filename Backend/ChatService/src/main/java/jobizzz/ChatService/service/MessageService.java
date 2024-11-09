package jobizzz.ChatService.service;



import jobizzz.ChatService.model.ChatMessage;
import jobizzz.ChatService.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(String roomId, ChatMessage message) {
        message.setTimestamp(Instant.now()); // Set the current timestamp
        messageRepository.saveMessage(roomId, message);
    }

    public List<Object> getChatHistory(String roomId) {
        return messageRepository.findMessages(roomId);
    }
}
