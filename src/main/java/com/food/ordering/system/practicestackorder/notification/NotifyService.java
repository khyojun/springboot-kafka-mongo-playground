package com.food.ordering.system.practicestackorder.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyService {

    private final ObjectMapper objectMapper;

    private final MongoTemplate mongoTemplate;

    @KafkaListener(topics = "payment-success", groupId = "payment-group")
    public void sendNotification(String message){
        //TODO : send notification
        try {
            log.info("Yeah! Payment success: {}", message);
            String orderId = parseMessage(message);
            Notify notify = Notify.ofSuccess(orderId, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            mongoTemplate.save(notify, "notification");

            // TODO : send Notify By Subscribe User

            log.info("send Notify By Subscribe Users");
        }catch (Exception e){
            log.error("Error sending notification: {}", e);
        }
    }

    private String parseMessage(String message) {
        try {
            Map<String, Object> map = objectMapper.readValue(message, Map.class);
           return map.get("orderId").toString();
        } catch (JsonProcessingException e) {
            log.error("parse error: {}", message);
            throw new RuntimeException(e);
        }

    }
}
