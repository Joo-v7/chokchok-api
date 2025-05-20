package com.chokchok.chokchokapi.product.dto.response;

import com.chokchok.chokchokapi.product.domain.Product;
import com.chokchok.chokchokapi.product.domain.ProductImage;
import com.chokchok.chokchokapi.product.domain.ProductInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 리시트 용 상품 response DTO
 * @param id
 * @param name
 * @param price
 * @param discountRate
 * @param description
 * @param brand
 * @param moistureLevel
 * @param images
 */
public record ProductSimpleResponseDto(
        Long id,
        String name,
        Integer price,
        Integer discountRate,
        String description,
        String brand,
        Float moistureLevel,
        List<String> images
) {

    // Product -> ProductSimpleResponseDto
    public static ProductSimpleResponseDto from(Product product) {
        List<String> images = new ArrayList<>();

        if(product.getImages() != null && !product.getImages().isEmpty()) {
            images = product.getImages().stream().map(ProductImage::getUrl).toList();
        }

        return new ProductSimpleResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDiscountRate(),
                product.getDescription(),
                product.getBrand(),
                product.getMoistureLevel(),
                images
        );
    }
}
