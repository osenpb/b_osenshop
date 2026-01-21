package com.osen.osenshop.core.cart.service.impl;


import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.common.handler_exception.exceptions.InsufficientStockException;
import com.osen.osenshop.core.cart.models.Cart;
import com.osen.osenshop.core.cart.models.CartItem;
import com.osen.osenshop.core.cart.repositories.CartRepository;
import com.osen.osenshop.core.cart.service.CartItemService;
import com.osen.osenshop.core.cart.service.CartService;
import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import com.osen.osenshop.core.product.model.Product;
import com.osen.osenshop.core.product.service.ProductService;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;

    public CartServiceImpl(CartRepository cartRepository, CartItemService cartItemService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }


    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + id));
    }
    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public Optional<Cart> findByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public List<CartItem> getCartItemsByUserId(Long userId) {
        return cartItemService.findByUserId(userId);
    }

    @Override
    public void addProduct(User user, Long productId, Integer quantity) throws InsufficientStockException {
        Cart cart = findOrCreateCart(user);

        Product product = productService.findById(productId);
        CartItem item = cartItemService
                .findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseGet(() -> createItem(cart, productId));

        int newQuantity = item.getQuantity() + quantity;

        if (newQuantity > product.getStock()) {
            log.warn(
                    "Stock insuficiente. Producto={}, stock={}, solicitado={}",
                    productId,
                    product.getStock(),
                    newQuantity
            );
            throw new InsufficientStockException("Producto: " + product.getName());
        }
        log.info("Producto guardado correctamente");
        item.setQuantity(item.getQuantity() + quantity);
        cartItemService.save(item);
    }


    private Cart findOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    private CartItem createItem(Cart cart, Long productId) {
        Product product = productService.findById(productId);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(0);
        return item;
    }

    @Transactional
    public void clearUserCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        cart.getCartItemList().clear();
    }
}
