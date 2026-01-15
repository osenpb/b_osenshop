package com.osen.osenshop.core.cart.models;


import com.osen.osenshop.core.product.model.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // muchos cartItems pueden referirse al mismo producto

    private Integer quantity;

    public Double subTotal() {
        return product.getPrice() * quantity;
    }

}
