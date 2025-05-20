package com.chokchok.chokchokapi.product.controller;

import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.product.dto.request.ProductRegisterRequestDto;
import com.chokchok.chokchokapi.product.dto.request.ProductUpdateRequestDto;
import com.chokchok.chokchokapi.product.dto.response.ProductDetailsResponseDto;
import com.chokchok.chokchokapi.product.service.ProductCommandService;
import com.chokchok.chokchokapi.product.service.ProductInventoryCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 등록/수정/삭제를 위한 RestController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductCommandController {

    private final ProductCommandService productCommandService;
    private final ProductInventoryCommandService productInventoryCommandService;

    /**
     * 상품 등록
     * @param productRegisterRequestDto
     * @return ProductRegisterResponseDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ProductDetailsResponseDto> register(
            @Valid @RequestBody ProductRegisterRequestDto productRegisterRequestDto
    ) {
        ProductDetailsResponseDto response = productCommandService.register(productRegisterRequestDto);
        return ResponseDto.<ProductDetailsResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    /**
     * 상품 정보 업데이트
     * @param id
     * @param productUpdateRequestDto
     * @return ResponseDto<ProductDetailsResponseDto>
     */
    @PutMapping("/{id}")
    public ResponseDto<ProductDetailsResponseDto> update(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequestDto productUpdateRequestDto
    ) {
        ProductDetailsResponseDto response = productCommandService.update(id, productUpdateRequestDto);
        return ResponseDto.<ProductDetailsResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 상품 soft 삭제 - 상품 isDeleted true로 변경
     * @param id
     * @return ResponseDto<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseDto<Void> delete(@PathVariable Long id) {
        productCommandService.delete(id);
        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

}
