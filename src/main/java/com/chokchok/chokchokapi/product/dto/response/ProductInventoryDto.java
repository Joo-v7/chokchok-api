package com.chokchok.chokchokapi.product.dto.response;

import com.chokchok.chokchokapi.product.domain.ProductInventory;

import java.time.LocalDateTime;

/**
 * 상품재고 응답에 사용되는 DTO
 */
public record ProductInventoryDto(
        Long id,
        Integer quantity,
        boolean isSoldOut,
        LocalDateTime updatedAt
) {

    // ProductInventory -> ProductInventoryDto
    public static ProductInventoryDto from(ProductInventory productInventory) {
        return new ProductInventoryDto(
                productInventory.getId(),
                productInventory.getQuantity(),
                productInventory.isSoldOut(),
                productInventory.getUpdatedAt()
        );
    }
}
