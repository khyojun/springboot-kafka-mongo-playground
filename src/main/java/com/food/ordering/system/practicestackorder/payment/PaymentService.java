package com.food.ordering.system.practicestackorder.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate kafkaTemplate;
    private final MongoTemplate mongoTemplate;

    @KafkaListener(topics = "create-order", groupId = "order-group")
    public void savePayment(String message){
        log.info("Payment event consumed: {}", message);
        Payment payment = parseMessage(message);
        mongoTemplate.save(payment, "payment");

        kafkaTemplate.send("payment-success", payment);
    }


    private Payment parseMessage(String message){

        try {
            Map<String, String> orderEvent = objectMapper.readValue(message, Map.class);

            String orderId = orderEvent.get("orderId");
            return new Payment(orderId, 1000);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
