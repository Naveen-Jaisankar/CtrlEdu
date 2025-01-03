//package com.ctrledu.ChatService.consumer;
//
//import java.util.concurrent.TimeUnit;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ctrledu.ChatService.constants.KafkaConstants;
//import com.ctrledu.ChatService.model.Message;
//
//@Component
//public class MessageListenerTopic2 {
//    @Autowired
//    SimpMessagingTemplate template;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//    ObjectMapper mapper = new ObjectMapper();
//    @KafkaListener(
//            topics = KafkaConstants.KAFKA_TOPIC2,
//            groupId = KafkaConstants.GROUP_ID_1,
//            containerFactory = "kafkaListenerContainerFactory2"
//    )
//    public void listen(Message message) {
//        int lastSeqNum = 0;
//        if(redisTemplate.opsForValue().get("topic2_last_seq_num") != null){
//            //System.out.println(redisTemplate.opsForValue().get("topic2_last_seq_num"));
//            lastSeqNum = Integer.parseInt((String) redisTemplate.opsForValue().get("topic2_last_seq_num"));
//        }
//
//        int currentSeqNum = lastSeqNum + 1;
//        String key = "class_" + message.getClassId() + "_module_" + message.getModuleId() + "_seq_" + currentSeqNum;
//        redisTemplate.opsForValue().set(key, message);
//        redisTemplate.opsForValue().set("class_" + message.getClassId() + "_module_" + message.getModuleId() + "_last_seq_num", Integer.toString(currentSeqNum));
//
////        redisTemplate.opsForValue().set("topic2_seq_" + currentSeqNum, message);
////        redisTemplate.opsForValue().set("topic2_last_seq_num", Integer.toString(currentSeqNum));
//        redisTemplate.expire("topic2_last_seq_num", 43200, TimeUnit.MINUTES);
//        redisTemplate.expire("topic2_seq_" + Integer.toString(currentSeqNum), 43200, TimeUnit.MINUTES);
//        //System.out.println(redisTemplate.opsForValue().get("topic2_last_seq_num"));
//        System.out.println("sending via kafka listener..");
//        System.out.println("Received message from dynamic topic: " + message.getTopic());
//        System.out.println("Received message for class: " + message.getClassId() + " and module: " + message.getModuleId());
//        // template.convertAndSend("/topic/group", message);
//        // Process message dynamically from any matching topic
//        template.convertAndSend("/topic/class_" + message.getClassId() + "_module_" + message.getModuleId(), message);
//
//    }
//}
