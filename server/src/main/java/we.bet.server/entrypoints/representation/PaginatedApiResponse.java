package we.bet.server.entrypoints.representation;

import java.util.List;

public class PaginatedApiResponse<T> {

    private List<T> content;
    private Long totalElements;
    private Integer totalPages;
    private Boolean isLastPage;


    public PaginatedApiResponse(List<T> content, Long totalElements, Integer totalPages, Boolean isLastPage) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLastPage = isLastPage;
    }

    public List<T> getContent() {
        return content;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Boolean isLastPage() {
        return isLastPage;
    }
}
