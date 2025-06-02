package com.chokchok.chokchokapi.category.controller;

import com.chokchok.chokchokapi.category.dto.request.CategoryOrderUpdateRequestDto;
import com.chokchok.chokchokapi.category.dto.request.CategoryRegisterRequestDto;
import com.chokchok.chokchokapi.category.dto.response.CategorySimpleResponseDto;
import com.chokchok.chokchokapi.category.service.CategoryCommandService;
import com.chokchok.chokchokapi.common.aop.annotation.CheckRole;
import com.chokchok.chokchokapi.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자용 카테고리 등록/수정/삭제 API를 제공하는 RestController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/categories")
public class CategoryCommandController {

    private final CategoryCommandService categoryCommandService;

    /**
     * 카테고리를 등록합니다.
     * @param categoryRegisterRequestDto
     * @return CategorySimpleResponseDto
     */
    @CheckRole(hasRole = "ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<CategorySimpleResponseDto> register(
            @Valid @RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto
            ) {
        CategorySimpleResponseDto response = categoryCommandService.register(categoryRegisterRequestDto);
        return ResponseDto.<CategorySimpleResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    /**
     * 카테고리 이름을 업데이트합니다.
     * @param id
     * @param name
     * @return CategorySimpleResponseDto
     */
    @CheckRole(hasRole = "ROLE_ADMIN")
    @PutMapping("/{id}/name")
    public ResponseDto<CategorySimpleResponseDto> updateName(
            @PathVariable Long id,
            @RequestParam String name
    ) {
        CategorySimpleResponseDto response = categoryCommandService.updateName(id, name);
        return ResponseDto.<CategorySimpleResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 해당 카테고리의 부모를 변경합니다.
     * @param id
     * @param categoryOrderUpdateRequestDto
     * @return
     */
    @CheckRole(hasRole = "ROLE_ADMIN")
    @PutMapping("/{id}/order")
    public ResponseDto<CategorySimpleResponseDto> updateOrder(
            @PathVariable Long id,
            @RequestBody CategoryOrderUpdateRequestDto categoryOrderUpdateRequestDto
    ) {
        CategorySimpleResponseDto response = categoryCommandService.updateOrder(id, categoryOrderUpdateRequestDto);
        return ResponseDto.<CategorySimpleResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 카테고리를 소프트 삭제합니다
     * @param id
     */
    @CheckRole(hasRole = "ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryCommandService.delete(id);
    }


}
