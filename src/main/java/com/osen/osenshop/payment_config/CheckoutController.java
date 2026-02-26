package com.osen.osenshop.payment_config;


import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.domain.services.UserService;
import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import com.osen.osenshop.core.order.dtos.OrderFormRequest;
import com.osen.osenshop.core.order.models.Order;
import com.osen.osenshop.core.order.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    public CheckoutController(PaymentService paymentService, OrderService orderService, UserService userService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> checkout(
            @RequestBody CheckoutRequest request,
            @AuthenticationPrincipal User user) {
        try {
            // 1. Obtener usuario autenticado
            User userFound = userService.findByEmail(user.getEmail()).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // 2. Procesar pago
            Payment payment = paymentService.createPayment(request.payment());

            if (!payment.getStatus().equals("approved")) {
                return ResponseEntity.badRequest()
                        .body("Pago rechazado: " + payment.getStatusDetail());
            }

            // 3. Si pago aprobado, crear la orden

            OrderFormRequest orderFormRequestFilled = new OrderFormRequest(request.order().shippingAddress());
            Order order = orderService.processOrder(userFound, orderFormRequestFilled);

            return ResponseEntity.ok().build();

        } catch (MPException | MPApiException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error de pago: " + e.getMessage());
        }  catch (Exception e) {

            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error inesperado: " + e.getMessage());
        }
    }
}