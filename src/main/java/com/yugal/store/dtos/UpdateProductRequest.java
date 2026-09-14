package com.yugal.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private String name;
    private String description;
    private Byte categoryId;
    private BigDecimal price;
}
