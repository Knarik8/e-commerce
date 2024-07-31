package com.epam.ecommerce.controller;


import com.epam.ecommerce.service.OrderService;
import com.epam.ecommerce.service.UserService;
import com.epam.ecommerce.entity.Order;
import com.epam.ecommerce.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    private UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestParam String username, @RequestBody Map<Long, Integer> productQuantities) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(orderService.placeOrder(user, productQuantities));
    }

    @GetMapping("/userOrders")
    public ResponseEntity<List<Order>> getUserOrders(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }
}
