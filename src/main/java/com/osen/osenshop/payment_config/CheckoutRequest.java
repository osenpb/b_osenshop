package com.osen.osenshop.payment_config;

import com.osen.osenshop.core.order.dtos.OrderFormRequest;

public record CheckoutRequest(
        PaymentRequest payment,  // token, amount, email
        OrderFormRequest order  // shippingAddress, items
) {
}