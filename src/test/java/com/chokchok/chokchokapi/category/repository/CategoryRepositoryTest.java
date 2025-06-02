package com.chokchok.chokchokapi.category.repository;

import com.chokchok.chokchokapi.category.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("루트 카테고리 저장")
    void register_root() {
        Category category = Category.create("스킨케어", null);

        Category savedCategory = categoryRepository.save(category);

        Assertions.assertThat(savedCategory.getId()).isEqualTo(category.getId());
        Assertions.assertThat(savedCategory.getName()).isEqualTo(category.getName());
        Assertions.assertThat(savedCategory.getDepth()).isEqualTo(0);
    }

    @Test
    @DisplayName("자식 카테고리 저장")
    void register_child() {
        Category parent = Category.create("스킨케어", null);
        Category child = Category.create("스킨", parent);

        parent.addChild(child);

        Category savedParent = categoryRepository.save(parent);
        Category savedChild = categoryRepository.save(child);

        Assertions.assertThat(savedChild.getDepth()).isEqualTo(1);
        List<Category> children = savedParent.getChildren();
        Assertions.assertThat(children).contains(savedChild);
    }
  
}