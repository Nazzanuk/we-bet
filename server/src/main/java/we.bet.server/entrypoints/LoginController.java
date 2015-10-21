package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.*;
import we.bet.server.core.usecase.login.WeBetUserService;

@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(method = RequestMethod.POST)
    public void login() {
        //basic auth in spring security
    }
    
}
