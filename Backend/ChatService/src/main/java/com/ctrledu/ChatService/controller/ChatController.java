package com.ctrledu.ChatService.controller;
import com.ctrledu.ChatService.constants.KafkaConstants;
import com.ctrledu.ChatService.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class ChatController {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/api/message", consumes = "application/json", produces = "application/json")
    public void sendMessage(@RequestBody Message message) throws InterruptedException, ExecutionException {
        //            kafkaTemplate.send(message.getTopic(), message).get();
        kafkaTemplate.send("class_" + message.getClassId() + "_module_" + message.getModuleId(), message);

    }

    @GetMapping(value = "/api/messages", produces = "application/json")
    public Map<String, Object> getMessages(@RequestParam String classId, @RequestParam String moduleId, @RequestParam int offset) {
        String baseKey = "class_" + classId + "_module_" + moduleId + "_";
        int lastSeqNum = Integer.parseInt((String) redisTemplate.opsForValue().get(baseKey + "last_seq_num"));
        List<Message> messages = new ArrayList<>();

        for (int i = lastSeqNum; i > 0 && i > lastSeqNum - offset; i--) {
            String key = baseKey + "seq_" + i;
            Message message = (Message) redisTemplate.opsForValue().get(key);
            if (message != null) messages.add(message);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("messages", messages);
        return response;
    }


    @GetMapping(value = "/api/message", produces = "application/json")
    public Map<String, Object> getMessages(@RequestParam int offset, @RequestParam String topic ) {
        System.out.println("Received Request - Topic: " + topic + ", Offset: " + offset);
        System.out.println("Topic Id: "+ topic);
        ArrayList<Message> messages = new ArrayList<Message>();
        int lastSeqNum = 0;

        // Retrieve last sequence number
        Object lastSeqNumObj = redisTemplate.opsForValue().get(topic + "_last_seq_num");
        System.out.println("Raw lastSeqNum from Redis: " + lastSeqNumObj);
        if(lastSeqNumObj != null){
            lastSeqNum = Integer.parseInt((String)redisTemplate.opsForValue().get(topic + "_last_seq_num"));
            System.out.println(lastSeqNum);
        }
        System.out.println("Parsed lastSeqNum: " + lastSeqNum);


        for (int i = lastSeqNum ; i > 0 && i > lastSeqNum - offset - 10; i--) {
            String key = topic + "_seq_" + i;
            System.out.println("Trying to fetch key: " + key);
            Object rawValue = redisTemplate.opsForValue().get(key);  // Fetch raw value from Redis
            System.out.println("Raw value for key: " + key + ": " + rawValue);

            if (rawValue != null) {
                try {
                    Message message = objectMapper.convertValue(rawValue, Message.class);
                    System.out.println("Deserialized message: " + message);
                    messages.add(message);
                } catch (Exception e) {
                    System.err.println("Error deserializing value for key: " + key);
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("No value found for key: " + key);
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("messages", messages);
        System.out.println("Final messages: " + messages);
        return response;
    }



    // WebSocket API

    @MessageMapping("/send")
    public void broadcastGroupMessage(@Payload Message message) {
        String topic = message.getTopic();
        System.out.println("Broadcasting to topic: " + topic);

        // Correctly broadcast to all subscribers on this topic
        template.convertAndSend("/topic/" + topic, message);
    }



    @MessageMapping("/newUser")
    @SendTo("/topic/group")
    public Message addUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

}
