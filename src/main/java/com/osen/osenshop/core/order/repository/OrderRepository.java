package com.osen.osenshop.core.order.repository;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.core.order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUser(User user);

}
