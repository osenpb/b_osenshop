package com.osen.osenshop.core.cart.dtos;

public record AddToCartRequest(
        Long productId,
        Integer quantity
) {
}
