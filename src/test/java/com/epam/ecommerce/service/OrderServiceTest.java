package com.epam.ecommerce.service;

import com.epam.ecommerce.entity.Order;
import com.epam.ecommerce.entity.User;
import com.epam.ecommerce.repository.OrderRepository;
import com.epam.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;


    @Test
    void whenGetOrdersByUserThenReturnListOfOrders() {
        User user = new User();
        user.setId(1L);

        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findByUser(user)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUser(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    void whenPlaceOrderWithInvalidProductThenThrowException() {

        User user = new User();
        user.setId(1L);

        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(1L, 2);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.placeOrder(user, productQuantities));
        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }
}
