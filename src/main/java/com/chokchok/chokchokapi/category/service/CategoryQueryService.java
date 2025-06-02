package com.chokchok.chokchokapi.category.service;

import com.chokchok.chokchokapi.category.domain.Category;
import com.chokchok.chokchokapi.category.dto.response.CategoryDetailsResponseDto;
import com.chokchok.chokchokapi.category.dto.response.CategorySimpleResponseDto;
import com.chokchok.chokchokapi.category.repository.CategoryRepository;
import com.chokchok.chokchokapi.common.dto.PaginatedResponseDto;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 카테고리 조회를 위한 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category getCategoryEntity(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND, "요청하신 카테고리를 찾을 수 없습니다.")
        );
    }

    /**
     * 카테고리 ID로 카테고리를 조회합니다.
     * @param categoryId
     * @return CategorySimpleResponseDto 해당 카테고리와 부모 카테고리 정보입니다.
     */
    @Transactional(readOnly = true)
    public CategorySimpleResponseDto findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND, "요청하신 카테고리를 찾을 수 없습니다.")
        );

        return CategorySimpleResponseDto.from(category);
    }

    /**
     * 카테고리를 리스트를 페이징하여 조회합니다.
     * @param pageable
     * @return PaginatedResponseDto<CategoryDetailsResponseDto> 페이징 처리된 카테고리로 해당 카테고리와 자식 카테고리 정보입니다.
     */
    @Transactional(readOnly = true)
    public PaginatedResponseDto<CategoryDetailsResponseDto> findAll(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);

        return getCategoryPaginatedResponses(page);
    }

    private PaginatedResponseDto<CategoryDetailsResponseDto> getCategoryPaginatedResponses(Page<Category> page) {
        List<CategoryDetailsResponseDto> allCategories = page.getContent().stream()
                .map(CategoryDetailsResponseDto::from)
                .toList();

        // 루트 카테고리(깊이 0)만 필터링
        List<CategoryDetailsResponseDto> rootCategories = allCategories.stream()
                .filter(c -> c.depth() == 0)
                .toList();

        return PaginatedResponseDto.<CategoryDetailsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(rootCategories)
                .build();
    }
}
