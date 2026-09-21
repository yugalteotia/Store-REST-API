package com.yugal.store.controllers;

import com.yugal.store.dtos.AddItemRequestDto;
import com.yugal.store.dtos.CartDto;
import com.yugal.store.dtos.CartItemDto;
import com.yugal.store.dtos.UpdateItemRequestDto;
import com.yugal.store.exceptions.CartNotFoundException;
import com.yugal.store.exceptions.ProductNotFoundException;
import com.yugal.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.createCart();
        return ResponseEntity.created(uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri()).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addItemToCart(@PathVariable UUID cartId, @RequestBody AddItemRequestDto request, UriComponentsBuilder uriBuilder) {
        var cartItemDto = cartService.addItemToCart(cartId, request.getProductId());
        return ResponseEntity.created(uriBuilder.path("/{cartId}/items").buildAndExpand(cartId).toUri()).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCartItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId, @Valid @RequestBody UpdateItemRequestDto request) {
        return cartService.updateCartItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "CartItem deleted successfully."));
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable("cartId") UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Cart cleared successfully."));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found."));
    }
}
