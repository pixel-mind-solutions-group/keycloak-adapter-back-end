package com.pixelmind.keycloak_adapter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PageResponse {
    private int totalPages;
    private int currentPage;
    private long totalElements;
    private Object dataList;
}
