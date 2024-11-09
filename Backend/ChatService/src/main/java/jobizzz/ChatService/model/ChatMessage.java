package jobizzz.ChatService.model;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ChatMessage implements Serializable {
    private String roomId;
    private String senderId;
    private String content;
    private Instant timestamp;  //
}
