package com.chokchok.chokchokapi.product.dto.response;

import com.chokchok.chokchokapi.product.domain.Product;
import com.chokchok.chokchokapi.product.domain.ProductImage;
import com.chokchok.chokchokapi.product.domain.ProductInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 등록 response DTO
 * @param name
 * @param price
 * @param discountRate
 * @param description
 * @param moistureLevel
 * @param images
 * @param quantity
 */
public record ProductRegisterResponseDto(
        Long id,
        String name,
        Integer price,
        Integer discountRate,
        String description,
        Float moistureLevel,
        List<String> images,
        Integer quantity,
        boolean isSoldOut
) {

    // Product -> ProductRegisterResponseDto
    public static ProductRegisterResponseDto from(Product product, ProductInventory productInventory) {
        List<String> images = new ArrayList<>();

        if(product.getImages() != null && !product.getImages().isEmpty()) {
            images = product.getImages().stream().map(ProductImage::getUrl).toList();
        }

        return new ProductRegisterResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDiscountRate(),
                product.getDescription(),
                product.getMoistureLevel(),
                images,
                productInventory.getQuantity(),
                productInventory.isSoldOut()
        );
    }
}
