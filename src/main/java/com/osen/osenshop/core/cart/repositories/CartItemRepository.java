package com.osen.osenshop.core.cart.repositories;

import com.osen.osenshop.core.cart.models.Cart;
import com.osen.osenshop.core.cart.models.CartItem;
import com.osen.osenshop.core.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.id = :id AND ci.cart.user.id = :userId")
    void deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    void deleteByCartId(Long cartId);

}
