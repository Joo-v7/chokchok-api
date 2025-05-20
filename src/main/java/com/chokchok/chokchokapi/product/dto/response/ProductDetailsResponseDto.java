package com.chokchok.chokchokapi.product.dto.response;

import com.chokchok.chokchokapi.product.domain.Product;
import com.chokchok.chokchokapi.product.domain.ProductImage;
import com.chokchok.chokchokapi.product.domain.ProductInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 response DTO
 * @param name
 * @param price
 * @param discountRate
 * @param description
 * @param moistureLevel
 * @param images
 * @param quantity
 */
public record ProductDetailsResponseDto(
        Long id,
        String name,
        Integer price,
        Integer discountRate,
        String description,
        String brand,
        Float moistureLevel,
        List<String> images,
        Integer quantity,
        boolean isSoldOut
) {

    // Product, quantity, isSoldOut -> ProductDetailsResponseDto
    public static ProductDetailsResponseDto from(Product product, Integer quantity, boolean isSoldOut) {
        List<String> images = new ArrayList<>();

        if(product.getImages() != null && !product.getImages().isEmpty()) {
            images = product.getImages().stream().map(ProductImage::getUrl).toList();
        }

        return new ProductDetailsResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDiscountRate(),
                product.getDescription(),
                product.getBrand(),
                product.getMoistureLevel(),
                images,
                quantity,
                isSoldOut
        );
    }
}
