package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.*;
import we.bet.server.core.usecase.login.LoginService;
import we.bet.server.entrypoints.exceptions.UnauthorizedException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void login(@RequestParam String username, @RequestParam String password) {
        if(!loginService.authenticate(username, password)){
            throw new UnauthorizedException();
        }
    }
    
}
