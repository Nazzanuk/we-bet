package we.bet.server.entrypoints.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private final String message;

    public NotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @ResponseBody
    NotFoundException handleBadRequest(HttpServletRequest req, Exception ex) {
        return new NotFoundException(ex.getMessage());
    }
}
