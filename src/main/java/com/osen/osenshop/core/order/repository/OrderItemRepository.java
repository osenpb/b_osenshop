package com.osen.osenshop.core.order.repository;

import com.osen.osenshop.core.order.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
