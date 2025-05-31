package com.productservice.mapper;

import com.productservice.dto.ProductDto;
import com.productservice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
}