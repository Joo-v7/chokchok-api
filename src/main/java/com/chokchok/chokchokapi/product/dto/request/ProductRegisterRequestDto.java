package com.chokchok.chokchokapi.product.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 상품등록 request DTO
 * @param name
 * @param price
 * @param discountRate
 * @param description
 * @param moistureLevel
 * @param images
 * @param quantity
 */
public record ProductRegisterRequestDto(
        String name,
        Integer price,
        Integer discountRate,
        String description,
        Float moistureLevel,
        List<String> images,
        Integer quantity
) {
}
