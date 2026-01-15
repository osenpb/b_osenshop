package com.osen.ecommerce.cart;


import com.osen.ecommerce.common.exceptions.EntityNotFoundException;
import com.osen.ecommerce.core.cart.models.CartItem;
import com.osen.ecommerce.core.cart.repositories.CartItemRepository;
import com.osen.ecommerce.core.cart.service.impl.CartItemServiceImpl;
import com.osen.ecommerce.core.product.model.Product;
import com.osen.ecommerce.core.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    void shouldReturnCartItemWhenProductExists(){
        Long cartId = 1L;
        Long productId = 2L;

        Product product = new Product();
        product.setId(productId);

        CartItem cartItem = new CartItem();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCart_IdAndProduct_Id(cartId
                ,productId)).thenReturn(Optional.of(cartItem));

        //When
        Optional<CartItem> result = cartItemService.findByCart_IdAndProduct_Id(cartId, productId);


        //Then
        assertTrue(result.isPresent());
        verify(productRepository).findById(productId);
        verify(cartItemRepository).findByCart_IdAndProduct_Id(cartId, productId);

    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        Long cartId = 1L;
        Long productId = 99L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                cartItemService.findByCart_IdAndProduct_Id(cartId, productId)
        );

        verify(cartItemRepository, never())
                .findByCart_IdAndProduct_Id(any(), any());
    }


}
