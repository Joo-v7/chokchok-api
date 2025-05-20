package com.chokchok.chokchokapi.product.repository;

import com.chokchok.chokchokapi.product.domain.Product;
import com.chokchok.chokchokapi.product.domain.ProductTypeCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByProductTypeCode(Pageable pageable, ProductTypeCode productTypeCode);
}
