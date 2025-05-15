package com.chokchok.chokchokapi.product.repository;

import com.chokchok.chokchokapi.product.domain.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
}
