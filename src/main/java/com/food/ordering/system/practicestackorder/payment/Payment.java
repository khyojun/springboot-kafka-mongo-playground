package com.food.ordering.system.practicestackorder.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Payment {

    private String orderId;

    private Integer amount;


}
