package com.ra.base_spring_boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private List<T> data;
}
