package com.chokchok.chokchokapi.product.service;

import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 조회를 담당하는 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QueryProductService {

    private final ProductRepository productRepository;

}
