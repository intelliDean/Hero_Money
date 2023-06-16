package com.loan.hero.hero_utility;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paginate<T> {
    private Long totalElements;
    private Long totalPages;
    private Long pageNumber;
    private Long pageSize;
    private List<T> content;
}
