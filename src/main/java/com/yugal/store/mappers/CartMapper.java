package com.yugal.store.mappers;

import com.yugal.store.dtos.CartDto;
import com.yugal.store.dtos.CartItemDto;
import com.yugal.store.entities.Cart;
import com.yugal.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "cartItems", target = "items")
    @Mapping(expression = "java(cart.getCartsTotalPrice())", target = "totalPrice")
    CartDto toDto(Cart cart);
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
