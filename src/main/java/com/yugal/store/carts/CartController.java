package com.yugal.store.carts;

import com.yugal.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carts", description = "The Cart API")
public class CartController {

    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.createCart();
        return ResponseEntity.created(uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri()).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add a product to the cart")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The ID of the cart.") @PathVariable UUID cartId,
            @RequestBody AddItemRequestDto request, UriComponentsBuilder uriBuilder) {
        var cartItemDto = cartService.addItemToCart(cartId, request.getProductId());
        return ResponseEntity.created(uriBuilder.path("/{cartId}/items").buildAndExpand(cartId).toUri()).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "Get the details of a cart")
    public CartDto getCart(@Parameter(description = "The ID of the cart.") @PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Update the quantity of a product in the cart")
    public CartItemDto updateCartItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId, @Valid @RequestBody UpdateItemRequestDto request) {
        return cartService.updateCartItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Remove a product from the cart")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "CartItem deleted successfully."));
    }

    @DeleteMapping("/{cartId}/items")
    @Operation(summary = "Clear all products from the cart")
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
