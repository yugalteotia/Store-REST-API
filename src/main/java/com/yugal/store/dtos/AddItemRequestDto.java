package com.yugal.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemRequestDto {
    @NotNull
    private long productId;
}
