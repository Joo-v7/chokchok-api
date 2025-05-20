package com.chokchok.chokchokapi.common.dto;

import lombok.*;

import java.util.List;

import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class PaginatedResponseDto<T> {
    private long totalPage;
    private long currentPage;
    private long totalDataCount;
    private List<T> dataList;
}
