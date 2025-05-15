package com.chokchok.chokchokapi.product.repository;

import com.chokchok.chokchokapi.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
