package jobizzz.ChatService.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jobizzz.ChatService.model.ChatMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisMessageSubscriber(SimpMessagingTemplate simpMessagingTemplate, ObjectMapper objectMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(message.getBody(), ChatMessage.class);
            String topic = "/topic/" + chatMessage.getRoomId();  // Use roomId from the message
            simpMessagingTemplate.convertAndSend(topic, chatMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
