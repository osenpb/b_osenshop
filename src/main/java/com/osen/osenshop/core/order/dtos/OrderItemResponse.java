package com.osen.osenshop.core.order.dtos;

import com.osen.osenshop.core.product.dtos.ProductResponse;

public record OrderItemResponse(
        Long id,
        Double price,
        Integer quantity,
        Long orderId,
        ProductResponse productResponse
) {
}
