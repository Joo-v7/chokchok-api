package com.chokchok.chokchokapi.category.dto.request;

/**
 * 카테고리 정렬 순서 변경을 요청하는 DTO
 * @param name
 * @param parentId
 */
public record CategoryOrderUpdateRequestDto(
        String name,
        Long parentId
) {
}
