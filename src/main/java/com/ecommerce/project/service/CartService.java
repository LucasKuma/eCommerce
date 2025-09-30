package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;
import org.springframework.stereotype.Service;

public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quant);
}
