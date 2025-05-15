package com.chokchok.chokchokapi.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "products")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer discountPrice;

    @Column(nullable = false)
    private String description;

    @Column
    private Float moistureLevel;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ProductInventory> inventories;

    private Product(String name, Integer price, Integer discountPrice, String description) {
        this.name = name;
        this.price = price;
        this.discountPrice = discountPrice;
        this.description = description;
    }

    /**
     * 상품을 생성하는 정적 팩토리 메소드
     * @param name
     * @param price
     * @param discountPrice
     * @param description
     * @return Product
     */
    public static Product create(String name, Integer price, Integer discountPrice, String description) {
        return new Product(name, price, discountPrice, description);
    }

    /**
     * 상품 soft deleted를 위해 isDeleted를 true로 바꿉니다.
     */
    public void delete() {
        this.isDeleted = true;
    }
}