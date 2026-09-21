package com.yugal.store.services;

import com.yugal.store.dtos.CartDto;
import com.yugal.store.dtos.CartItemDto;
import com.yugal.store.entities.Cart;
import com.yugal.store.exceptions.CartNotFoundException;
import com.yugal.store.exceptions.ProductNotFoundException;
import com.yugal.store.mappers.CartMapper;
import com.yugal.store.repositories.CartRepository;
import com.yugal.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {

    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        return cartMapper.toDto(cartRepository.save(new Cart()));
    }

    public CartItemDto addItemToCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, Integer quantity) {

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        var cartItem = cart.getItem(productId);
        if (cartItem == null) throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void deleteCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();
        if (cart.removeItem(productId)) cartRepository.save(cart);
        else throw new ProductNotFoundException();
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();
        cart.clearCart();
        cartRepository.save(cart);
    }
}
