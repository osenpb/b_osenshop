package com.osen.osenshop.core.order.services;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.core.order.dtos.OrderFormRequest;
import com.osen.osenshop.core.order.models.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();
    Order findById(Long id);
    Order save(Order order);
    void deleteById(Long id);
    Order processOrder(User user, OrderFormRequest orderForm);
    List<Order> findByUser(User user);
    void updateStatusOrder(Long orderId);
}
