package com.chokchok.chokchokapi.category.dto.response;

import com.chokchok.chokchokapi.category.domain.Category;

import java.util.List;

/**
 * 카테고리 상세 정보를 담는 DTO
 * @param id
 * @param name
 * @param depth 카테고리 깊이(0 == 루트)
 * @param children 자식 카테고리 목록(삭제된 카테고리는 제외)
 */
public record CategoryDetailsResponseDto(
        Long id,
        String name,
        Integer depth,
        List<CategoryDetailsResponseDto> children
) {
    // Category -> CategoryDetailsResponseDto
    public static CategoryDetailsResponseDto from(Category category) {
        return new CategoryDetailsResponseDto(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getChildren().stream()
                        .filter(child -> !child.isDeleted())
                        .map(CategoryDetailsResponseDto::from)
                        .toList()
        );
    }
}
