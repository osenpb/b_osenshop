package com.osen.osenshop.core.order.services;

import com.osen.osenshop.core.order.models.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> findAll();
    OrderItem findById(Long id);
    OrderItem save(OrderItem orderItem);
    void deleteById(Long id);

}
