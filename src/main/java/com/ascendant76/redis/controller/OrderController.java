package com.ascendant76.redis.controller;

import com.ascendant76.redis.entities.Order;
import com.ascendant76.redis.respository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderController {

    private OrderRepository orderRepository;

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable("id") String id) {
        return orderRepository.findById(id).orElseThrow();
    }
}