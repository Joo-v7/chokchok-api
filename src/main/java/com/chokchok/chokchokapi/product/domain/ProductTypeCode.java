package com.chokchok.chokchokapi.product.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 상품 유형 코드의 enum 클래스압나다.
 */
@Getter
@RequiredArgsConstructor
public enum ProductTypeCode {
    NONE(1, "없음"),
    BESTSELLER(2, "베스트셀러"),
    RECOMMENDATION(3, "추천"),
    NEW(4, "신상"),
    POPULARITY(5, "인기"),
    DISCOUNTS(6, "할인");

    private final int id;
    private final String koName;
}
