package com.restropos.systemorder.service;

import com.restropos.systemorder.entity.Order;
import com.restropos.systemshop.constants.UserTypes;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderSubscriptionManager {
    private final Map<UserTypes, FluxSink<Order>> subscriptionMap = new ConcurrentHashMap<>();

    public void subscribe(UserTypes userType, FluxSink<Order> fluxSink) {
        subscriptionMap.put(userType, fluxSink);
    }

    public void unsubscribe(UserTypes userType) {
        subscriptionMap.remove(userType);
    }

    public void notifySubscribers(Order order, UserTypes userType) {
        FluxSink<Order> fluxSink = subscriptionMap.get(userType);
        if (fluxSink != null) {
            fluxSink.next(order);
        }
    }
}
