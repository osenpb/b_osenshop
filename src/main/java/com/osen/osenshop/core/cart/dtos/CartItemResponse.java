package com.osen.osenshop.core.cart.dtos;

import com.osen.osenshop.core.product.dtos.ProductResponse;

public record CartItemResponse(
        Long id,
        Long cartId,
        ProductResponse productResponse,
        Integer quantity,
        Double subtotal
) {
}
