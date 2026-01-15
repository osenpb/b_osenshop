package com.osen.osenshop.core.cart.mapper;

import com.osen.osenshop.core.cart.dtos.CartResponse;
import com.osen.osenshop.core.cart.models.Cart;

public class CartMapper {


    public static CartResponse toDto(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(), // aqui podria  ser userResponse, pero creo que ya desde el authService esta controlado
                CartItemMapper.toListDto(cart.getCartItemList()),
                cart.getTotal()
        );
    }
}
