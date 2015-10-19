package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.usecase.login.LoginService;
import we.bet.server.entrypoints.exceptions.ConflictException;
import we.bet.server.entrypoints.exceptions.UnauthorizedException;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private LoginService loginService;

    public RegisterController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void register(@RequestParam String username, @RequestParam String password) {
        if(!loginService.register(username, password)){
            throw new ConflictException();
        }
    }
    
}
