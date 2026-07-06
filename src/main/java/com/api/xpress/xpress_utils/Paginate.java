package com.api.xpress.xpress_utils;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Builder
public record Paginate<T> (
        Long totalElements,
        Long totalPages,
        Long pageNumber,
        Long pageSize,
        List<T> content
) {
    public static <T> Paginate<T> fromPage(Page<T> page) {
        return Paginate.<T>builder()
                .totalElements(page.getTotalElements())
                .totalPages((long) page.getTotalPages())
                .pageNumber((long) page.getNumber())
                .pageSize((long) page.getSize())
                .content(page.getContent())
                .build();
    }
}