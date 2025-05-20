package com.chokchok.chokchokapi.product.controller;

import com.chokchok.chokchokapi.common.dto.PaginatedResponseDto;
import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.product.domain.ProductTypeCode;
import com.chokchok.chokchokapi.product.dto.response.ProductDetailsResponseDto;
import com.chokchok.chokchokapi.product.dto.response.ProductSimpleResponseDto;
import com.chokchok.chokchokapi.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 조회를 위한 RestController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    /**
     * 상품 상세 정보를 조회합니다.
     * @param id
     * @return 상품의 상세 정보가 담긴 응답 객체
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseDto<ProductDetailsResponseDto> getProductDetails(@PathVariable Long id) {
        ProductDetailsResponseDto response = productQueryService.findProductById(id);

        return ResponseDto.<ProductDetailsResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 상품 목록을 페이지네이션 하여 조회합니다.
     * 상품 타입이 있다면 이로 필터랑하여 조회합니다.
     * @param pageable
     * @param productTypeCode
     * @return 상품 목록과 페이징 정보를 담은 응답 객체
     */
    @Transactional(readOnly = true)
    @GetMapping
    public ResponseDto<PaginatedResponseDto<ProductSimpleResponseDto>> getProducts(
            Pageable pageable,
            @RequestParam(required = false) ProductTypeCode productTypeCode
    ) {
        PaginatedResponseDto<ProductSimpleResponseDto> response = productQueryService.findAll(pageable, productTypeCode);

        return ResponseDto.<PaginatedResponseDto<ProductSimpleResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }


}
