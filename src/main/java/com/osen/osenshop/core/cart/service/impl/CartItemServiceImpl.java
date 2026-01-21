package com.osen.osenshop.core.cart.service.impl;

import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import com.osen.osenshop.core.cart.models.CartItem;
import com.osen.osenshop.core.cart.repositories.CartItemRepository;
import com.osen.osenshop.core.cart.service.CartItemService;
import com.osen.osenshop.core.product.model.Product;
import com.osen.osenshop.core.product.repository.ProductRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product not fount with id: " + productId));
        return cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        cartItemRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    @Transactional
    public void deleteAllCartItemsByCartId(Long cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }
}
