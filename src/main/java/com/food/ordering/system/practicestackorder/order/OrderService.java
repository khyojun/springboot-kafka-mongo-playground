package com.food.ordering.system.practicestackorder.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService{

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper obj;
    private final KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "orders-request", groupId = "order-group")
    @Transactional
    void receive(String message){
        log.info("Order event consumed: {}", message);
        OrderEvent orderEvent = parseMessage(message);

        Order order = Order.of(orderEvent.orderId(), orderEvent.orderName(), orderEvent.orderContent());
        mongoTemplate.save(order, "order");
        log.info("save order to mongo db: {}", order);
        kafkaTemplate.send("create-order", orderEvent);

        log.info("send create-event consumed: {}", orderEvent);
        log.info("Create order event published to Kafka: {}", orderEvent);
    }

    private OrderEvent parseMessage(String message) {
        try {
            return obj.readValue(message, OrderEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing message: {}", message);
            throw new RuntimeException(e);
        }
    }
}
