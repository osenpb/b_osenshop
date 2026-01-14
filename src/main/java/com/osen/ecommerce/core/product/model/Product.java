package com.osen.ecommerce.core.product.model;


import com.osen.ecommerce.core.category.model.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_products_name",
                        columnNames = "name"
                )
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
