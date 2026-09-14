package com.yugal.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
