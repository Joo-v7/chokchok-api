package com.chokchok.chokchokapi.product.converter;

import com.chokchok.chokchokapi.common.exception.base.InvalidException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.product.domain.ProductTypeCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

/**
 * enum과 DB의 정수 값 간의 변환을 처리하는 컨버터입니다
 */
@Converter
public class ProductTypeCodeConverter implements AttributeConverter<ProductTypeCode, Integer> {

    /**
     * 주어진 enum 상수를 DB에 어떤 값으로 넣을 것인지 찾아 리턴합니다.
     * @param productTypeCode
     * @return Integer
     */
    @Override
    public Integer convertToDatabaseColumn(ProductTypeCode productTypeCode) {
        return productTypeCode.getId();
    }

    /**
     * DB에서 읽힌 id에 따라 어떤 enum이랑 매칭시킬것인지 찾아 리턴합니다.
     * @param id
     * @return ProductTypeCode
     */
    @Override
    public ProductTypeCode convertToEntityAttribute(Integer id) {
        return Arrays.stream(ProductTypeCode.values())
                .filter(code -> id.equals(code.getId()))
                .findAny()
                .orElseThrow(() -> new InvalidException(ErrorCode.INVALID_PRODUCT_TYPE_CODE, "유효하지 않은 상품 유형 코드입니다: " + id));
    }
}
