package com.yugal.store.services;

import com.yugal.store.dtos.CheckoutRequest;
import com.yugal.store.dtos.CheckoutResponse;
import com.yugal.store.entities.Order;
import com.yugal.store.exceptions.CartEmptyException;
import com.yugal.store.exceptions.CartNotFoundException;
import com.yugal.store.repositories.CartRepository;
import com.yugal.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {


    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) throw new CartNotFoundException();
        if (cart.isEmpty()) throw new CartEmptyException();

        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        cartService.clearCart(request.getCartId());

        return new CheckoutResponse(order.getId());
    }
}
