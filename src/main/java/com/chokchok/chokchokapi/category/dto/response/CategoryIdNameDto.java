package com.chokchok.chokchokapi.category.dto.response;

/**
 * 카테고리 ID와 Name을 담는 DTO 입니다.
 * @param id
 * @param name
 */
public record CategoryIdNameDto(
        Long id,
        String name
) {
}
