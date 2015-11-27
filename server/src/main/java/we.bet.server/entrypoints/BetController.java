package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.domain.bet.Bet;
import we.bet.server.core.usecase.bet.BetService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;

import java.security.Principal;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/bet")
public class BetController {

    private final BetService betService;
    private final WeBetUserService weBetUserService;

    public BetController(BetService betService, WeBetUserService weBetUserService) {
        this.betService = betService;
        this.weBetUserService = weBetUserService;
    }

    @RequestMapping(method = POST)
    public PaginatedApiResponse<Bet> getAllBets(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int pageSize, Principal principal) {
        String username = principal.getName();
        if(username == null || isEmpty(username)){
            throw new BadRequestException("Invalid parameter value");
        }

        UUID userId = weBetUserService.getIdForUser(username);
        return betService.getAllBets(userId, page, pageSize);
    }

    @RequestMapping(value = "/{betId}", method = GET)
    public ApiResponse getBet(@PathVariable String betId, Principal principal) {
        String username = principal.getName();
        if(isEmpty(username) || isEmpty(betId)){
            throw new BadRequestException("Invalid parameter value");
        }

        UUID userId = weBetUserService.getIdForUser(username);
        Bet bet = betService.getBet(userId, fromString(betId));
        return new ApiResponse(asList(bet));
    }

    @RequestMapping(value = "/accept/{betId}", method = POST)
    public void acceptBet(@PathVariable String betId, Principal principal) {
        String username = principal.getName();
        if(isEmpty(betId) || isEmpty(username)){
            throw new BadRequestException("Invalid parameter value");
        }

        UUID userId = weBetUserService.getIdForUser(username);
        betService.acceptBet(userId, fromString(betId));
    }

    @RequestMapping(value = "/delete/{betId}", method = DELETE)
    public void deleteBet(@PathVariable String betId, Principal principal) {
        String username = principal.getName();
        if(isEmpty(betId) || isEmpty(username)){
            throw new BadRequestException("Invalid parameter value");
        }
        UUID userId = weBetUserService.getIdForUser(username);
        betService.deleteBet(userId, fromString(betId));
    }

    @RequestMapping(value = "/create", method = POST)
    public ApiResponse createBet(
            @RequestParam String createdFor,
            @RequestParam String title,
            @RequestParam String description,
            Principal principal) {
        String username = principal.getName();
        if(username == null || createdFor == null || isEmpty(username) || isEmpty(createdFor)){
            throw new BadRequestException("Invalid parameter value");
        }

        UUID userId = weBetUserService.getIdForUser(username);
        return new ApiResponse(asList(betService.create(userId, fromString(createdFor), title, description)));
    }
    
}
