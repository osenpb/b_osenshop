package com.osen.osenshop.core.order.mappers;

import com.osen.osenshop.core.order.dtos.OrderItemResponse;
import com.osen.osenshop.core.order.models.OrderItem;
import com.osen.osenshop.core.product.mapper.ProductMapper;

import java.util.List;

public class OrderItemMapper {

    public static OrderItemResponse toDto(OrderItem orderItem){
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getOrder().getId(),
                ProductMapper.toDto(orderItem.getProduct())
        );
    }

    public static List<OrderItemResponse> toListDto(List<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItemMapper::toDto).toList();
    }

}
