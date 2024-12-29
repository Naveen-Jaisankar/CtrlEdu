package com.ctrledu.ChatService.consumer;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ctrledu.ChatService.constants.KafkaConstants;
import com.ctrledu.ChatService.model.Message;

@Component
public class MessageListenerTopic3 {
    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    ObjectMapper mapper = new ObjectMapper();
    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC3,
            groupId = KafkaConstants.GROUP_ID_1,
            containerFactory = "kafkaListenerContainerFactory3"
    )
    public void listen(Message message) {
        int lastSeqNum = 0;
        if(redisTemplate.opsForValue().get("topic3_last_seq_num") != null){
            //System.out.println(redisTemplate.opsForValue().get("topic3_last_seq_num"));
            lastSeqNum = Integer.parseInt((String) redisTemplate.opsForValue().get("topic3_last_seq_num"));
        }

        int currentSeqNum = lastSeqNum + 1;
        redisTemplate.opsForValue().set("topic3_seq_" + currentSeqNum, message);
        redisTemplate.opsForValue().set("topic3_last_seq_num", Integer.toString(currentSeqNum));
        redisTemplate.expire("topic3_last_seq_num", 43200, TimeUnit.MINUTES);
        redisTemplate.expire("topic3_seq_" + Integer.toString(currentSeqNum), 43200, TimeUnit.MINUTES);
        //System.out.println(redisTemplate.opsForValue().get("topic3_last_seq_num"));
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/group", message);
    }
}
