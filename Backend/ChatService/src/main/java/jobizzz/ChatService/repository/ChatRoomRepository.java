package jobizzz.ChatService.repository;


import jobizzz.ChatService.model.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomRepository {

    private static final String KEY = "ChatRoom";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(ChatRoom chatRoom) {
        redisTemplate.opsForHash().put(KEY, chatRoom.getRoomId(), chatRoom);
    }

    public ChatRoom findById(String roomId) {
        return (ChatRoom) redisTemplate.opsForHash().get(KEY, roomId);
    }
}
