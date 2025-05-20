package com.chokchok.chokchokapi.product.service;

import com.chokchok.chokchokapi.common.exception.base.ConflictException;
import com.chokchok.chokchokapi.common.exception.base.InvalidException;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.product.domain.Product;
import com.chokchok.chokchokapi.product.domain.ProductInventory;
import com.chokchok.chokchokapi.product.dto.response.ProductInventoryDto;
import com.chokchok.chokchokapi.product.repository.ProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품재고 등록, 수정을 위한 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductInventoryCommandService {

    private final ProductInventoryRepository productInventoryRepository;

    /**
     * 상품 등록 시 사용되는 상품재고 등록 메소드 입니다.
     * @param product
     * @param quantity
     * @return ProductInventory
     */
    @Transactional
    public ProductInventoryDto register(Product product, Integer quantity) {
        ProductInventory productInventory = ProductInventory.create(product, quantity);
        ProductInventory result = saveInventory(productInventory);

        return ProductInventoryDto.from(result);
    }

    /**
     * 상품재고 수량, 매진 여부를 수정합니다.
     * @param productId
     * @param requiredQuantity
     * @param soldOut
     * @return ProductInventory
     */
    @Transactional
    public ProductInventoryDto update(Long productId, Integer requiredQuantity, boolean soldOut) {
        if(requiredQuantity <= 0) {
            throw new InvalidException(ErrorCode.INVALID_PRODUCT_QUANTITY, "업데이트할 상품 수량은 1 이상이어야 합니다.");
        }

        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        // update quantity, isSoldOut
        productInventory.updateQuantity(requiredQuantity);
        productInventory.updateSoldOut(soldOut);
        productInventory.updateUpdatedAt();
        // save
        ProductInventory result = saveInventory(productInventory);

        return ProductInventoryDto.from(result);
    }

    /**
     * 상품재고 수량을 차감합니다.
     * @param productId
     * @param requiredQuantity - 차감할 상품 개수
     * @return Integer - 차감된 상품 개수
     */
    @Transactional
    public void deductQuantity(Long productId, int requiredQuantity) {
        if(requiredQuantity <= 0) {
            throw new InvalidException(ErrorCode.INVALID_PRODUCT_QUANTITY, "차감할 상품 수량은 1 이상이어야 합니다.");
        }

        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        int oldQuantity = productInventory.getQuantity();
        int deductedQuantity = oldQuantity - requiredQuantity;

        if(deductedQuantity < 0) {
            throw new InvalidException(ErrorCode.INVALID_PRODUCT_QUANTITY, "현재 남은 수량: " + oldQuantity + ", 요구한 수량: " + requiredQuantity);
        } else if (oldQuantity - requiredQuantity == 0) {
            productInventory.updateSoldOut(true);
        }

        // product quantity update
        productInventory.updateQuantity(deductedQuantity);
        // save
        saveInventory(productInventory);
    }

    /**
     * 상품재고 수량을 업데이트합니다.
     * @param productId
     * @param requiredQuantity
     * @return int 업데이트된 상품재고 수량
     */
    @Transactional
    public int updateQuantity(Long productId, int requiredQuantity) {
        if(requiredQuantity <= 0) {
            throw new InvalidException(ErrorCode.INVALID_PRODUCT_QUANTITY, "업데이트할 상품 수량은 1 이상이어야 합니다.");
        }

        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        productInventory.updateQuantity(requiredQuantity);
        productInventory.updateUpdatedAt();
        // save
        ProductInventory result = saveInventory(productInventory);
        return result.getQuantity();
    }

    /**
     * 상품재고를 매진 처리합니다.
     * @param productId
     */
    @Transactional
    public void markAsSoldOut(Long productId) {
        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        productInventory.updateSoldOut(true);
        productInventory.updateUpdatedAt();
        // save
        saveInventory(productInventory);
    }

    /**
     * 상품재고의 매진을 해제합니다.
     * @param productId
     */
    @Transactional
    public void markAsAvailable(Long productId) {
        ProductInventory productInventory = productInventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_INVENTORY_NOT_FOUND, "해당 상품의 상품재고를 찾을 수 없습니다: " + productId)
        );

        productInventory.updateSoldOut(false);
        productInventory.updateUpdatedAt();
        // save
        saveInventory(productInventory);
    }

    /**
     * 상품 재고 정보를 저장합니다.
     * @param inventory
     * @return ProductInventory
     */
    private ProductInventory saveInventory(ProductInventory inventory) {
        try {
            return productInventoryRepository.save(inventory);
        } catch(DataIntegrityViolationException e) {
            log.error("상품재고 저장 중 데이터 무결성 위반 발생: {}", e.getMessage());
            throw new ConflictException(ErrorCode.PRODUCT_INVENTORY_ALREADY_EXISTS, "상품 재고가 이미 존재합니다.");
        } catch(Exception e) {
            log.error("상품재고 저장 중 알 수 없는 오류 발생", e);
            throw new RuntimeException("상품재고 저장 중 알 수 없는 오류가 발생했습니다.");
        }
    }


}
