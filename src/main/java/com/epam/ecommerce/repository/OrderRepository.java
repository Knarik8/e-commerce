package com.epam.ecommerce.repository;

import com.epam.ecommerce.entity.Order;
import com.epam.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}
