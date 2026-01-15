package com.osen.osenshop.core.cart.repositories;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.core.cart.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
