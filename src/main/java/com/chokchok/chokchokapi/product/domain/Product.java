package com.chokchok.chokchokapi.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Integer discountRate;

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
    private List<ProductImage> images = new ArrayList<>();

    private Product(String name, Integer price, Integer discountRate, String description, Float moistureLevel) {
        this.name = name;
        this.price = price;
        this.discountRate = discountRate;
        this.description = description;
        this.moistureLevel = Objects.requireNonNullElse(moistureLevel, 0F);
    }

    /**
     * 상품을 생성하는 정적 팩토리 메소드
     * @param name
     * @param price
     * @param discountRate
     * @param description
     * @return Product
     */
    public static Product create(String name, Integer price, Integer discountRate, String description, Float moistureLevel) {
        return new Product(name, price, discountRate, description, moistureLevel);
    }

    /**
     * 상품에 이미지 정보를 추가합니다.
     * @param image
     */
    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this); // 연관관계 양방향 동기화
    }

    /**
     * 상품 soft deleted를 위해 isDeleted를 true로 바꿉니다.
     */
    public void delete() {
        this.isDeleted = true;
    }
}