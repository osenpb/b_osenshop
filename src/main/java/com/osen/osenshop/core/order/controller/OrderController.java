package com.osen.osenshop.core.order.controller;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.domain.services.UserService;
import com.osen.osenshop.common.handler_exception.exceptions.AccessDeniedException;
import com.osen.osenshop.core.cart.service.CartItemService;
import com.osen.osenshop.core.cart.service.CartService;
import com.osen.osenshop.core.order.dtos.OrderFormRequest;
import com.osen.osenshop.core.order.dtos.OrderItemResponse;
import com.osen.osenshop.core.order.dtos.OrderResponse;
import com.osen.osenshop.core.order.mappers.OrderItemMapper;
import com.osen.osenshop.core.order.mappers.OrderMapper;
import com.osen.osenshop.core.order.models.Order;
import com.osen.osenshop.core.order.models.OrderItem;
import com.osen.osenshop.core.order.services.OrderItemService;
import com.osen.osenshop.core.order.services.OrderService;
import com.osen.osenshop.core.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
       List<Order> orders = orderService.findAll();
       List<OrderResponse> orderResponseList = OrderMapper.toOrderDtoList(orders);
       return ResponseEntity.ok(orderResponseList);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/checkout")
    public ResponseEntity<?> procesarPedido(@AuthenticationPrincipal User user, @RequestBody OrderFormRequest orderForm) {
        Order successfulOrder = orderService.processOrder(user, orderForm);
        OrderResponse orderResponse = OrderMapper.toDto(successfulOrder);
        log.info("Compra realizada con exito");
        return ResponseEntity.ok(orderResponse);

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/order-success/{id}")
    public ResponseEntity<?> confirmacionPedido(@AuthenticationPrincipal User user, @PathVariable Long id) {

        Order order = orderService.findById(id);

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Acceso denegado");
        }

        List<OrderItem> orderItems = order.getOrderItemList();
        List<OrderItemResponse> orderItemResponseList = OrderItemMapper.toListDto(orderItems);

        return ResponseEntity.ok(orderItemResponseList);

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> ordenesFromUser(@AuthenticationPrincipal User user) {
        User myUser = userService.findById(user.getId());
        List<Order> orders = orderService.findByUser(user);
        List<OrderResponse> orderResponseList = OrderMapper.toOrderDtoList(orders);

        return ResponseEntity.ok(orderResponseList);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatusOrder(@RequestBody Long orderId){
        orderService.updateStatusOrder(orderId);
        return ResponseEntity.ok().build();
    }

}
