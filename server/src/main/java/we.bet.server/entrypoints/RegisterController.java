package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.ConflictException;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private WeBetUserService weBetUserService;

    public RegisterController(WeBetUserService weBetUserService) {
        this.weBetUserService = weBetUserService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void register(@RequestParam String username, @RequestParam String password) {
        weBetUserService.register(username, password);
    }
    
}
