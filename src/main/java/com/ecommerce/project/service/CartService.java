package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quant);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);
}
