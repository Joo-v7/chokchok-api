package com.chokchok.chokchokapi.product.service;

import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.product.domain.ProductInventory;
import com.chokchok.chokchokapi.product.dto.response.ProductInventoryDto;
import com.chokchok.chokchokapi.product.repository.ProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품재고 조회를 담당하는 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductInventoryQueryService {

    private final ProductInventoryRepository productInventoryRepository;

    /**
     * 상품 ID를 기반으로 상품 재고 정보를 조회하고 반환합니다.
     * @param productId
     * @return ProductInventory
     */
    @Transactional(readOnly = true)
    public ProductInventoryDto getProductInventoryByProductId(Long productId) {
        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        return ProductInventoryDto.from(productInventory);
    }

    /**
     * 상품 ID를 통해 해당 상품의 재고 수량을 리턴합니다.
     * @param productId
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer findProductInventoryQuantityByProductId(Long productId) {
        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        return productInventory.getQuantity();
    }

    /**
     * 상품 ID를 통해 해당 상품의 재고가 매진 상태인지 여부를 리턴합니다.
     * @param productId
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean isSoldOut(Long productId) {
        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        return productInventory.isSoldOut();
    }

}
