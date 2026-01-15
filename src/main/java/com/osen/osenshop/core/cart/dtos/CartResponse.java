package com.osen.osenshop.core.cart.dtos;

import com.osen.osenshop.core.cart.models.CartItem;

import java.util.List;

public record CartResponse(
        Long id,
        Long userId,
        List<CartItemResponse> cartItemsResponse,
        Double total
) {
}
