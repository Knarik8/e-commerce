package com.epam.ecommerce.service;

import com.epam.ecommerce.entity.Order;
import com.epam.ecommerce.entity.OrderItem;
import com.epam.ecommerce.entity.Product;
import com.epam.ecommerce.entity.User;
import com.epam.ecommerce.repository.OrderRepository;
import com.epam.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class OrderService {

    private OrderRepository orderRepository;

    private ProductRepository productRepository;

    OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order placeOrder(User user, Map<Long, Integer> productQuantities) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productRepository.findById(entry.getKey()).orElseThrow();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(entry.getValue());
            orderItem.setPrice(product.getPrice() * entry.getValue());

            totalAmount += orderItem.getPrice();
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
