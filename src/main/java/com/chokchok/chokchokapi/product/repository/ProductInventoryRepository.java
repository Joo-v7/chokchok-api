package com.chokchok.chokchokapi.product.repository;

import com.chokchok.chokchokapi.product.domain.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    Optional<ProductInventory> findByProductId(Long productId);
}
