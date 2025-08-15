package com.invest.messageInQueueCreator.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> content;
    private long total;
    private Pageable pageable;

    public PaginationResponse(List<MovementResponse> response, long totalElements, int page, int size) {
        this.content = (List<T>) response;
        this.total = totalElements;
        this.pageable = new Pageable(page, size);
    }


    public static <T> PaginationResponse<T> of(List<T> content, long total, int page, int size) {
        PaginationResponse<T> response = new PaginationResponse<>();
        response.setContent(content);
        response.setTotal(total);
        response.setPageable(new Pageable(page, size));
        return response;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pageable {
        private int page;
        private int size;
    }
}
