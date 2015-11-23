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
        Optional<UUID> uuidOptional = weBetUserService.getIdForUser(principal.getName());
        if(uuidOptional.isPresent()){
            Map map = new HashMap<>();
            map.put("id", uuidOptional.get());
            return new ApiResponse(asList(map));
        }
        else{
            throw new UnauthorizedException();
        }
    }
    
}
