package com.yugal.store.controllers;

import com.yugal.store.dtos.AddItemRequestDto;
import com.yugal.store.dtos.CartDto;
import com.yugal.store.dtos.CartItemDto;
import com.yugal.store.dtos.UpdateItemRequestDto;
import com.yugal.store.entities.Cart;
import com.yugal.store.mappers.CartMapper;
import com.yugal.store.repositories.CartRepository;
import com.yugal.store.repositories.ProductRepository;
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

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartMapper.toDto(cartRepository.save(new Cart()));
        return ResponseEntity.created(uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri()).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addItemToCart(@PathVariable UUID cartId, @RequestBody AddItemRequestDto request, UriComponentsBuilder uriBuilder) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) return ResponseEntity.badRequest().build();

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return ResponseEntity.created(uriBuilder.path("/{cartId}/items").buildAndExpand(cart.getId()).toUri()).body(cartMapper.toDto(cartItem));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId, @Valid @RequestBody UpdateItemRequestDto request) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
        );
        var cartItem = cart.getItem(productId);
        if (cartItem == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Product was not found in the cart.")
        );

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
        );

        if (cart.removeItem(productId))
            cartRepository.save(cart);
        else return
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("error", "CartItem not found.")
                );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "CartItem deleted successfully."));
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable("cartId") UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
        );
        cart.clearCart();
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Cart cleared successfully."));
    }
}
