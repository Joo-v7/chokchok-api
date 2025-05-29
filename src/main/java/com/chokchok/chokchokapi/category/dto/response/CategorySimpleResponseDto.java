package com.chokchok.chokchokapi.category.dto.response;

import com.chokchok.chokchokapi.category.domain.Category;

/**
 * 카테고리 응답 DTO
 * @param id
 * @param name
 * @param depth
 * @param parentId
 * @param parentName
 */
public record CategorySimpleResponseDto(
        Long id,
        String name,
        Integer depth,
        Long parentId,
        String parentName
) {
    // Category -> CategoryResponseDto
    public static CategorySimpleResponseDto from(Category category) {
        Long parentId = null;
        String parentName = null;
        if (category.getParent() != null) {
            parentId = category.getParent().getId();
            parentName = category.getParent().getName();
        }

        return new CategorySimpleResponseDto(
                category.getId(),
                category.getName(),
                category.getDepth(),
                parentId,
                parentName
        );
    }
}