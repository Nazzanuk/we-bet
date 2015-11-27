package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.*;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.UnauthorizedException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final WeBetUserService weBetUserService;

    public LoginController(WeBetUserService weBetUserService) {
        this.weBetUserService = weBetUserService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse login(Principal principal) {
        UUID userId = weBetUserService.getIdForUser(principal.getName());
        Map map = new HashMap<>();
        map.put("id", userId);
        return new ApiResponse(asList(map));
    }
    
}
