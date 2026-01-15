package com.osen.osenshop.core.order.models;

import com.osen.osenshop.core.product.model.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double price;

    private Integer quantity;

    @ManyToOne
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // muchos cartItems pueden referirse al mismo producto

    public Double subTotal() {
        return price*quantity;
    }
}
