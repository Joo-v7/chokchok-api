package com.chokchok.chokchokapi.category.dto.request;

/**
 * 카테고리 등록을 요청하는 DTO
 * @param name
 * @param parentId
 */
public record CategoryRegisterRequestDto(
        String name,
        Long parentId
) {
}
