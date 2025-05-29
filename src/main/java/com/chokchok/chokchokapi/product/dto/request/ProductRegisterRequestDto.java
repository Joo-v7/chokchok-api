package com.chokchok.chokchokapi.product.dto.request;

import com.chokchok.chokchokapi.product.domain.ProductTypeCode;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 상품등록 request DTO
 * @param name
 * @param price
 * @param discountRate
 * @param description
 * @param brand
 * @param moistureLevel
 * @param productTypeCode
 * @param images
 * @param quantity
 */
public record ProductRegisterRequestDto(
        @NotBlank(message = "상품 이름은 필수 입력 사항입니다.")
        @Size(max = 255, message = "상품 이름은 최대 255자까지 입력할 수 있습니다.")
        String name,

        @NotNull(message = "상품 정가는 필수 입력 사항입니다.")
        @Min(value = 0, message = "상품 정가는 0 이상이어야 합니다.")
        Integer price,

        @NotNull(message = "상품 할인율은 필수 입력 사항입니다.")
        @Min(value = 0, message = "상품 할인율은 0 이상이어야 합니다.")
        @Max(value = 100, message = "상품 할인율은 100 이하여야 합니다.")
        Integer discountRate,

        @NotBlank(message = "상품 설명은 필수 입력 사항입니다.")
        @Size(max = 255, message = "상품 설명은 최대 255자까지 입력할 수 있습니다.")
        String description,

        @NotBlank(message = "상품 브랜드는 필수 입력 사항입니다.")
        @Size(max = 30, message = "상품 브랜드는 최대 30자까지 입력할 수 있습니다.")
        String brand,

        Float moistureLevel,

        ProductTypeCode productTypeCode,

        List<String> images,

        @NotNull(message = "상품 카테고리는 필수 선택 사항입니다.")
        Long categoryId,

        @NotNull(message = "상품 수량은 필수 입력 사항입니다.")
        @Min(value = 1, message = "상품 수량은 1개 이상이어야 합니다.")
        Integer quantity
) {
}
