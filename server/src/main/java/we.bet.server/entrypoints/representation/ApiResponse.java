package we.bet.server.entrypoints.representation;

import java.util.List;

public class ApiResponse<T> {

    private List<T> content;

    public ApiResponse(List<T> content) {
        this.content = content;
    }

    public List<T> getContent() {
        return content;
    }
}
