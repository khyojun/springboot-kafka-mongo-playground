package com.food.ordering.system.practicestackorder.order;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderContorller {

    private final KafkaTemplate kafkaTemplate;

    @PostMapping("/order")
    public String order(@RequestBody OrderEvent request){

        log.info("Order event published: {}", request);
        kafkaTemplate.send("orders-request", request);
        return "Order send to Kafka successfully";
    }
}
