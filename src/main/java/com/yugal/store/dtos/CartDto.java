package com.yugal.store.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    private Set<CartItemDto> items = new HashSet<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
