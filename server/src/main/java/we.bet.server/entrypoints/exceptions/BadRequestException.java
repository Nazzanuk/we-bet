package we.bet.server.entrypoints.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private String message;

    public BadRequestException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @ResponseBody BadRequestException handleBadRequest(HttpServletRequest req, Exception ex) {
        return new BadRequestException(ex.getMessage());
    }

}
