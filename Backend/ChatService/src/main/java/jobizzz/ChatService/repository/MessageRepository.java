package jobizzz.ChatService.repository;


import jobizzz.ChatService.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveMessage(String roomId, ChatMessage message) {
        redisTemplate.opsForList().rightPush("RoomMessages:" + roomId, message);
    }

    public List<Object> findMessages(String roomId) {
        return redisTemplate.opsForList().range("RoomMessages:" + roomId, 0, -1);
    }
}
