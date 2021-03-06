package we.bet.server.entrypoints.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    private final String message;

    public ConflictException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @ResponseBody
    ConflictException handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ConflictException(ex.getMessage());
    }
}
