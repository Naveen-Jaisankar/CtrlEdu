package jobizzz.ChatService.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PubSubService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void publishMessage(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
