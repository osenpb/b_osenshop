package com.osen.osenshop.core.order.services.impl;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.common.handler_exception.exceptions.InsufficientStockException;
import com.osen.osenshop.core.cart.models.CartItem;
import com.osen.osenshop.core.cart.service.CartItemService;
import com.osen.osenshop.core.cart.service.CartService;
import com.osen.osenshop.core.order.dtos.OrderFormRequest;
import com.osen.osenshop.core.order.models.Order;
import com.osen.osenshop.core.order.models.OrderItem;
import com.osen.osenshop.core.order.repository.OrderRepository;
import com.osen.osenshop.core.order.services.OrderItemService;
import com.osen.osenshop.core.order.services.OrderService;

import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import com.osen.osenshop.core.product.model.Product;
import com.osen.osenshop.core.product.repository.ProductRepository;
import com.osen.osenshop.core.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final ProductRepository productRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }
    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }


    private List<CartItem> ensureCartItemsInStock(User user) {
        List<CartItem> cartItems = cartService.getCartItemsByUserId(user.getId());

        for (CartItem cartItem : cartItems) {
            Product product = productService.findById(cartItem.getProduct().getId());
            if (product.getStock() < cartItem.getQuantity()) {
                log.error("Stock insuficiente para el producto: {}", product.getName());
                throw new InsufficientStockException("Not enough stock: " + product.getName());
            }
        }
        return cartItems;
    }

    public Order createPendingOrder(User user, OrderFormRequest orderFormRequest, Double total) {

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setShippingAddress(orderFormRequest.shippingAddress());
        order.setTotal(total);
        return orderRepository.save(order);
    }


    public void processCartItemsToOrderItems(List<CartItem> cartItems, Order savedOrder) {
        for (CartItem cartItem : cartItems) {
            Product product = productService.findById(cartItem.getProduct().getId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); // Precio al momento de la compra

            orderItemService.save(orderItem);

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
    }


    @Transactional
    public Order processOrder(User user, OrderFormRequest orderForm) {

        List<CartItem> cartItems = ensureCartItemsInStock(user);

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order savedOrder = createPendingOrder(user, orderForm, total);

        processCartItemsToOrderItems(cartItems, savedOrder);

        cartService.clearUserCart(user);

        return savedOrder;
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public void updateStatusOrder(Long orderId) {
        Order order = findById(orderId);
        if(order.getStatus().equals("PENDING")){ order.setStatus("DELIVERED"); }
        if(order.getStatus().equals("DELIVERED")){ order.setStatus("PENDING"); }
        save(order);

    }


}
