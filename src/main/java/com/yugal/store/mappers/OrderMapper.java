package com.yugal.store.mappers;

import com.yugal.store.dtos.OrderDto;
import com.yugal.store.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "items", source = "orderItems")
    OrderDto toDto(Order order);
}
