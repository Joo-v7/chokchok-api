package com.chokchok.chokchokapi.product.dto.request;

import com.chokchok.chokchokapi.product.domain.ProductTypeCode;
import jakarta.validation.constraints.*;

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
public record ProductUpdateRequestDto(
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

        @Min(value = 0, message = "상품 보습감은 0 이상이어야 합니다.")
        Float moistureLevel,

        @NotNull(message = "상품 유형은 필수 입력 사항입니다.")
        ProductTypeCode productTypeCode,

        List<String> images,

        @NotNull(message = "상품 수량은 필수 입력 사항입니다.")
        @Min(value = 1, message = "상품 수량은 1개 이상이어야 합니다.")
        Integer quantity,

        @NotNull(message = "상품 매진 여부는 필수 입력 사항입니다.")
        boolean isSoldOut
) {
}
