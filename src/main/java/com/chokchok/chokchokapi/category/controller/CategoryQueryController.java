package com.chokchok.chokchokapi.category.controller;

import com.chokchok.chokchokapi.category.dto.response.CategoryDetailsResponseDto;
import com.chokchok.chokchokapi.category.dto.response.CategorySimpleResponseDto;
import com.chokchok.chokchokapi.category.service.CategoryQueryService;
import com.chokchok.chokchokapi.common.dto.PaginatedResponseDto;
import com.chokchok.chokchokapi.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 카테고리 조회를 위한 RestController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    /**
     * 단일 카테고리 조회
     *
     * @param id
     * @return CategorySimpleResponseDto
     */
    @GetMapping("/{id}")
    public ResponseDto<CategorySimpleResponseDto> getCategory(@PathVariable("id") Long id) {
        CategorySimpleResponseDto response = categoryQueryService.findCategoryById(id);
        return ResponseDto.<CategorySimpleResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 카테고리 리스트를 페이징 조회합니다.
     *
     * @param pageable
     * @return PaginatedResponseDto<CategoryDetailsResponseDto>
     */
    @GetMapping
    public ResponseDto<PaginatedResponseDto<CategoryDetailsResponseDto>> getCategoriesPage(Pageable pageable) {
        PaginatedResponseDto<CategoryDetailsResponseDto> response = categoryQueryService.findAll(pageable);
        return ResponseDto.<PaginatedResponseDto<CategoryDetailsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 카테고리를 트리 형태로 전체 조회합니다.
     *
     * @return List<CategoryDetailsResponseDto>
     */
    @GetMapping("/all")
    public ResponseDto<List<CategoryDetailsResponseDto>> getAllCategories() {
        List<CategoryDetailsResponseDto> response = categoryQueryService.findAll();

        return ResponseDto.<List<CategoryDetailsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }


}
