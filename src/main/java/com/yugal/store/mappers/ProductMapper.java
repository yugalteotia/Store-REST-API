package com.yugal.store.mappers;

import com.yugal.store.dtos.AddProductRequest;
import com.yugal.store.dtos.ProductDto;
import com.yugal.store.dtos.UpdateProductRequest;
import com.yugal.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    Product toEntity(AddProductRequest request);

    Product updateProduct(UpdateProductRequest request, @MappingTarget Product product);
}
