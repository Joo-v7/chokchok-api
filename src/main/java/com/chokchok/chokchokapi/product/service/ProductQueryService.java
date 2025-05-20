package com.chokchok.chokchokapi.product.service;

import com.chokchok.chokchokapi.common.dto.PaginatedResponseDto;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.product.domain.Product;
import com.chokchok.chokchokapi.product.domain.ProductTypeCode;
import com.chokchok.chokchokapi.product.dto.response.ProductDetailsResponseDto;
import com.chokchok.chokchokapi.product.dto.response.ProductInventoryDto;
import com.chokchok.chokchokapi.product.dto.response.ProductSimpleResponseDto;
import com.chokchok.chokchokapi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 상품 조회를 담당하는 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductQueryService {

    private final ProductRepository productRepository;

    private final ProductInventoryQueryService productInventoryQueryService;

    /**
     * 상품 ID로 상품을 조회합니다.
     * @param productId
     * @return ProductResponseDto
     */
    @Transactional(readOnly = true)
    public ProductDetailsResponseDto findProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "Product Not Found with id: " + productId)
        );

        ProductInventoryDto productInventoryDto = productInventoryQueryService.getProductInventoryByProductId(productId);

        return ProductDetailsResponseDto.from(product, productInventoryDto.quantity(), productInventoryDto.isSoldOut());
    }

    /**
     * 상품을 페이징하여 조회합니다.
     * 상품 유형 코드가 주어지면 해당 유형에 해당하는 상품만 조회하며, 주어지지 않은 경우 전체 상품을 조회합니다.
     * @param pageable
     * @param productTypeCode ProductTypeCode
     * @return PaginatedResponseDto<ProductResponseDto>
     */
    @Transactional(readOnly = true)
    public PaginatedResponseDto<ProductSimpleResponseDto> findAll(Pageable pageable, ProductTypeCode productTypeCode) {
        Page<Product> page;
        if (Objects.isNull(productTypeCode)) {
            page = productRepository.findAll(pageable);
        } else {
            page = productRepository.findAllByProductTypeCode(pageable, productTypeCode);
        }

        return getProductPaginatedResponses(page);
    }

    /**
     * 전체 조회된 page 객체를 바탕으로 전체 조회 화면에 내보낼 정보를 담은 dto page 객체를 반환합니다.
     * @param page
     * @return PaginatedResponseDto<ProductSimpleResponseDto>
     */
    private PaginatedResponseDto<ProductSimpleResponseDto> getProductPaginatedResponses(Page<Product> page) {
        List<ProductSimpleResponseDto> products = page.getContent().stream()
                .map(ProductSimpleResponseDto::from)
                .toList();

        return PaginatedResponseDto.<ProductSimpleResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
    }

}
