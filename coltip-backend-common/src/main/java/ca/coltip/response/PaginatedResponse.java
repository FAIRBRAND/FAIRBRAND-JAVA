package ca.coltip.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginatedResponse {
    @JsonProperty("totalElements")
    private int totalElements;
    @JsonProperty("totalPages")
    private int totalPages;
    @JsonProperty("pageNumber")
    private int pageNumber;
    @JsonProperty("pageSize")
    private int pageSize;

    public PaginatedResponse(int totalElements, int totalPages, int pageNumber, int pageSize) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
