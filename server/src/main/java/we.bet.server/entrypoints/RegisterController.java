package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private WeBetUserService weBetUserService;

    public RegisterController(WeBetUserService weBetUserService) {
        this.weBetUserService = weBetUserService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse register(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String firstname,
                                @RequestParam String lastname) {
        UUID id = weBetUserService.register(username, password, firstname, lastname);
        Map map = new HashMap<>();
        map.put("userId", id);
        return new ApiResponse(asList(map));
    }
    
}
