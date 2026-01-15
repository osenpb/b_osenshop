package com.osen.osenshop.core.cart.service;

import com.osen.osenshop.core.cart.models.Cart;
import com.osen.osenshop.core.cart.models.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService {

    List<CartItem> findAll();
    CartItem findById(Long id);
    CartItem save(CartItem cartItem);
    void deleteById(Long id);
    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
    List<CartItem> findByUserId(Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
    void deleteAllCartItemsByCartId(Long cartId);

}
