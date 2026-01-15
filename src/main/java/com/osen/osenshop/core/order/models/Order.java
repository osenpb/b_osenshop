package com.osen.osenshop.core.order.models;


import com.osen.osenshop.auth.domain.models.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private Double total;

    private String status = "PENDING";

    private String shippingAddress;

    private LocalDateTime createdAt = LocalDateTime.now();

    // PayMethod in future

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();

    public Double getTotal(){
        return orderItemList.stream().mapToDouble(OrderItem::subTotal).sum();
    }


}
