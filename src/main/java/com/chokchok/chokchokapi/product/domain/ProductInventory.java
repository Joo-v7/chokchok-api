package com.chokchok.chokchokapi.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "product_inventories")
@Entity
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private boolean isSoldOut;

    @Column
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private ProductInventory(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * 상품 재고를 생성하는 정적 팩토리 메소드
     * @param product
     * @param quantity
     * @return ProductInventory
     */
    public static ProductInventory create(Product product, Integer quantity) {
        return new ProductInventory(
                product,
                quantity
        );
    }

    /**
     * 상품재고 수량을 업데이트합니다.
     * @param quantity
     */
    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 상품 재고 매진 여부를 업데이트합니다.
     * @param isSoldOut
     */
    public void updateSoldOut(boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    /**
     * 상품재고 업데이트 일시를 업데이트합니다.(수량 변경시)
     */
    public void updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

}

