package com.osen.osenshop.core.cart.service;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.core.cart.models.Cart;
import com.osen.osenshop.core.cart.models.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartService {
    Cart findById(Long id);
    Cart save(Cart cart);
    void deleteById(Long id);
    Optional<Cart> findByUser(User user);
    List<CartItem> getCartItemsByUserId(Long userId);
    void addProduct(User user, Long aLong, Integer quantity);

    void clearUserCart(User user);
}
