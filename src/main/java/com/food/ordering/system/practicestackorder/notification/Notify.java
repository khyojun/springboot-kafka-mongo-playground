package com.food.ordering.system.practicestackorder.notification;

import java.time.LocalDateTime;

public record Notify(String orderId, Status status, LocalDateTime notifyTime) {
    public static Notify ofSuccess(String orderId, LocalDateTime notifyTime)
    {
      return new Notify(orderId, Status.SUCCESS, notifyTime);
    }


    public static Notify ofFail(String orderId, LocalDateTime notifyTime)
    {
        return new Notify(orderId, Status.FAILED, notifyTime);
    }
}


enum Status{
    SUCCESS,
    FAILED
}