package com.food.ordering.system.practicestackorder.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {


    @Id
    private String orderId;

    private String orderName;

    private String orderContent;


    @Builder
    private Order(String orderId, String orderName, String orderContent) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderContent = orderContent;
    }

    public static Order of(String orderId, String orderName, String orderContent)
    {
      return Order.builder()
              .orderId(orderId)
              .orderName(orderName)
              .orderContent(orderContent)
              .build();
    }
}
