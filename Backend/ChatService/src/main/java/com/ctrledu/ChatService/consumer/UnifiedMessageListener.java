package com.ctrledu.ChatService.consumer;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.ctrledu.ChatService.model.Message;

@Component
public class UnifiedMessageListener {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @KafkaListener(
            topicPattern = "class_.*_module_.*",
            groupId = "group1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(Message message) {
        processMessage(message);
    }

    private void processMessage(Message message) {
        int lastSeqNum = 0;
        String baseKey = "class_" + message.getClassId() + "_module_" + message.getModuleId();
        String lastSeqKey = baseKey + "_last_seq_num";

        // Fetch last sequence number
        if (redisTemplate.opsForValue().get(lastSeqKey) != null) {
            lastSeqNum = Integer.parseInt((String) redisTemplate.opsForValue().get(lastSeqKey));
        }

        // Increment sequence number and store the message
        int currentSeqNum = lastSeqNum + 1;
        String messageKey = baseKey + "_seq_" + currentSeqNum;
        redisTemplate.opsForValue().set(messageKey, message);
        redisTemplate.opsForValue().set(lastSeqKey, Integer.toString(currentSeqNum));

        // Set expiration times
        redisTemplate.expire(lastSeqKey, 43200, TimeUnit.MINUTES);
        redisTemplate.expire(messageKey, 43200, TimeUnit.MINUTES);

        // Log and broadcast message
        System.out.println("Received message from dynamic topic: " + message.getTopic());
        System.out.println("Received message for class: " + message.getClassId() + " and module: " + message.getModuleId());
        template.convertAndSend("/topic/" + baseKey, message);
    }
}
