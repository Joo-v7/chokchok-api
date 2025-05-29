package com.chokchok.chokchokapi.category.repository;

import com.chokchok.chokchokapi.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    boolean existsById(Long id);
}
