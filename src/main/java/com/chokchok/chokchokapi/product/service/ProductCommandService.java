package com.chokchok.chokchokapi.product.service;

import com.chokchok.chokchokapi.common.exception.base.ConflictException;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.product.domain.Product;
import com.chokchok.chokchokapi.product.domain.ProductImage;
import com.chokchok.chokchokapi.product.domain.ProductInventory;
import com.chokchok.chokchokapi.product.dto.request.ProductRegisterRequestDto;
import com.chokchok.chokchokapi.product.dto.request.ProductUpdateRequestDto;
import com.chokchok.chokchokapi.product.dto.response.ProductDetailsResponseDto;
import com.chokchok.chokchokapi.product.dto.response.ProductInventoryDto;
import com.chokchok.chokchokapi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 등록, 수정, 삭제를 위한 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final ProductInventoryCommandService productInventoryCommandService;

    /**
     * 상품을 생성하여 저장합니다.
     * @param requestDto
     * @return ProductResponseDto 생성된 상품 객체
     */
    @Transactional
    public ProductDetailsResponseDto register(ProductRegisterRequestDto requestDto) {
        // Product
        Product product = Product.create(
                requestDto.name(),
                requestDto.price(),
                requestDto.discountRate(),
                requestDto.description(),
                requestDto.brand(),
                requestDto.moistureLevel(),
                requestDto.productTypeCode()
        );

        // ProductImage
        if(requestDto.images() != null && !requestDto.images().isEmpty()) {
            requestDto.images().forEach(image -> {
                ProductImage productImage = ProductImage.create(product, image);
                product.addImage(productImage);
            });
        }

        // Product save
        Product savedProduct = saveProduct(product);

        // ProductInventory save
        ProductInventoryDto savedProductInventoryDto = productInventoryCommandService.register(savedProduct, requestDto.quantity());

        return ProductDetailsResponseDto.from(savedProduct, savedProductInventoryDto.quantity(), savedProductInventoryDto.isSoldOut());
    }

    /**
     * 상품 정보를 수정하고, 수정된 상품 정보를 DTO로 반환합니다.
     * @param id
     * @param requestDto
     * @return ProductDetailsResponseDto
     */
    @Transactional
    public ProductDetailsResponseDto update(Long id, ProductUpdateRequestDto requestDto) {
        // 기존 product
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "Product Not Found with id: " + id)
        );

        // producrImage update
        if (requestDto.images() != null && !requestDto.images().isEmpty()) {
            requestDto.images().forEach(image -> {
                ProductImage productImage = ProductImage.create(product, image);
                product.addImage(productImage);
            });
        }

        // productInventory update & save
        ProductInventoryDto productInventoryDto = productInventoryCommandService.update(id, requestDto.quantity(), requestDto.isSoldOut());

        // product save
        Product savedProduct = saveProduct(product);

        return ProductDetailsResponseDto.from(savedProduct, productInventoryDto.quantity(), productInventoryDto.isSoldOut());
    }

    /**
     * 상품을 삭제상태로 변경합니다.
     * @param id
     */
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "Product Not Found with id: " + id)
        );

        product.delete();
        // save
        saveProduct(product);
    }

    /**
     * 상품을 등록합니다.
     * @param product
     * @return Product
     */
    private Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            log.error("상품 등록 중 데이터 무결성 위반 발생: {}", e.getMessage());
            throw new ConflictException(ErrorCode.PRODUCT_ALREADY_EXISTS, "상품이 이미 존재합니다.");
        } catch (Exception e) {
            log.error("상품 등록 중 알 수 없는 오류 발생: {}", e.getMessage());
            throw new RuntimeException("상품등록 중 알 수 없는 오류가 발생했습니다.");
        }
    }

}
