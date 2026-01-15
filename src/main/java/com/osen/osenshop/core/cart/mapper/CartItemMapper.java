package com.osen.osenshop.core.cart.mapper;

import com.osen.osenshop.core.cart.dtos.CartItemResponse;

import com.osen.osenshop.core.cart.models.CartItem;
import com.osen.osenshop.core.product.mapper.ProductMapper;

import java.util.List;

public class CartItemMapper {

    public static CartItemResponse toDto(CartItem cartItem){
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getCart().getId(),
                ProductMapper.toDto(cartItem.getProduct()),
                cartItem.getQuantity(),
                cartItem.subTotal()
        );
    }

    public static List<CartItemResponse> toListDto(List<CartItem> cartItemList) {
        return cartItemList.stream().map(CartItemMapper::toDto).toList();
    }

}
