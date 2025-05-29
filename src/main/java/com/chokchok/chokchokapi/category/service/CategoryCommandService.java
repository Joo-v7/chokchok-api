package com.chokchok.chokchokapi.category.service;

import com.chokchok.chokchokapi.category.domain.Category;
import com.chokchok.chokchokapi.category.dto.request.CategoryOrderUpdateRequestDto;
import com.chokchok.chokchokapi.category.dto.request.CategoryRegisterRequestDto;
import com.chokchok.chokchokapi.category.dto.response.CategorySimpleResponseDto;
import com.chokchok.chokchokapi.category.repository.CategoryRepository;
import com.chokchok.chokchokapi.common.exception.base.ConflictException;
import com.chokchok.chokchokapi.common.exception.base.InvalidException;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 카테고리 등록/수정/삭제를 위한 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;

    /**
     * 새로운 카테고리를 등록합니다.
     *
     * @param categoryRegisterRequestDto
     * @return CategorySimpleResponseDto
     */
    @Transactional
    public CategorySimpleResponseDto register(CategoryRegisterRequestDto categoryRegisterRequestDto) {
        Category parentCategory = null;

        if(existsCategoryName(categoryRegisterRequestDto.name())) {
            throw new ConflictException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS, "카테고리 이름이 이미 존재합니다.");
        }

        if(categoryRegisterRequestDto.parentId() != null) {
            parentCategory = categoryRepository.findById(categoryRegisterRequestDto.parentId()).orElseThrow(
                    () -> new NotFoundException(ErrorCode.CATEGORY_PARENT_NOT_FOUND, "요청하신 카테고리의 부모 카테고리를 찾을 수 없습니다: " + categoryRegisterRequestDto.parentId())
            );
        }

        Category category = Category.create(categoryRegisterRequestDto.name(), parentCategory);
        if(parentCategory != null) {
            parentCategory.addChild(category);
        }

        Category result = saveCategory(category);

        return CategorySimpleResponseDto.from(result);
    }

    /**
     * 주어진 이름을 가진 카테고리가 이미 존재하는지 여부를 확인합니다.
     *
     * @param name 카테고리 이름
     * @return true: 이름이 존재함 / false: 존재하지 않음
     */
    private boolean existsCategoryName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * 카테고리의 이름을 변경합니다.
     *
     * @param id
     * @param name
     * @return CategorySimpleResponseDto
     */
    @Transactional
    public CategorySimpleResponseDto updateName(Long id, String name) {
        if(existsCategoryName(name)) {
            throw new ConflictException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS, "카테고리 이름이 이미 존재합니다.");
        }

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND, "요청하신 카테고리를 찾을 수 없습니다.")
        );

        category.updateName(name);

        Category result = saveCategory(category);

        return CategorySimpleResponseDto.from(result);
    }

    /**
     * 해당 카테고리의 부모를 변경합니다.
     *
     * @param id
     * @param categoryOrderUpdateRequestDto
     * @return CategorySimpleResponseDto
     */
    @Transactional
    public CategorySimpleResponseDto updateOrder(Long id, CategoryOrderUpdateRequestDto categoryOrderUpdateRequestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND, "요청하신 카테고리를 찾을 수 없습니다.")
        );

        Category parentCategory = null;
        if(categoryOrderUpdateRequestDto.parentId() != null) {
            parentCategory = categoryRepository.findById(categoryOrderUpdateRequestDto.parentId()).orElseThrow(
                    () -> new NotFoundException(ErrorCode.CATEGORY_PARENT_NOT_FOUND, "요청하신 카테고리의 부모 카테고리를 찾을 수 없습니다: " + categoryOrderUpdateRequestDto.parentId())
            );
        }

        // 순환 참조 방지 (자기 자신이 부모가 될 수 없음)
        if(category.equals(parentCategory)) {
            throw new InvalidException(ErrorCode.INVALID_CATEGORY_PARENT, "카테고리의 부모는 자기 자신이 될 수 없습니다.");
        }

        // 순환 참조 방지 (카테고리 자식에 수정할 parentCategory가 존재해서는 안된다)
        if(isCircular(category, parentCategory)) {
            throw new InvalidException(ErrorCode.INVALID_CATEGORY_PARENT, "카테고리 순환 참조가 발생했습니다.");
        }

        int newDepth = 0;
        if(parentCategory != null) {
            newDepth = parentCategory.getDepth() + 1;
        }

        category.updateParent(parentCategory);
        category.updateDepth(newDepth);

        Category result = saveCategory(category);

        return CategorySimpleResponseDto.from(result);
    }

    /**
     * 순환 참조 방지를 위한 검증 메서드입니다.
     * 새로운 부모가 해당 카테고리의 자식에 있으면 순환 문제 발생합니다.
     *
     * @param category
     * @param newParent
     * @return boolean
     */
    private boolean isCircular(Category category, Category newParent) {
        while(newParent != null) {
            if(newParent.equals(category)) {
                return true;
            }
            newParent = newParent.getParent();
        }
        return false;
    }

    /**
     * 카테고리를 소프트 삭제합니다.
     *
     * @param categoryId
     */
    @Transactional
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND, "요청하신 카테고리를 찾을 수 없습니다.")
        );

        category.delete();
        for(Category child : category.getChildren()) {
            child.delete();
        }
    }

    /**
     * 카테고리를 저장하고, 예외를 처리합니다.
     *
     * @param category
     * @return Category
     */
    private Category saveCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            log.error("카테고리 등록 중 데이터 무결성 위반 발생: {}", e.getMessage());
            throw new ConflictException(ErrorCode.PRODUCT_ALREADY_EXISTS, "카테고리가 이미 존재합니다.");
        } catch (Exception e) {
            log.error("카테고리 등록 중 알 수 없는 오류 발생: {}", e.getMessage());
            throw new RuntimeException("카테고리 등록 중 오류가 발생했습니다.");
        }
    }

}
