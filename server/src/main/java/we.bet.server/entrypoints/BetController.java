package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.domain.bet.Bet;
import we.bet.server.core.usecase.bet.BetService;
import we.bet.server.entrypoints.exceptions.BadRequestException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
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

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @RequestMapping(method = POST)
    public PaginatedApiResponse<Bet> getAllBets(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int pageSize,
            @RequestParam String id) {
        if(id == null || isEmpty(id)){
            throw new BadRequestException("Invalid parameter value");
        }
        return betService.getAllBets(fromString(id), page, pageSize);
    }

    @RequestMapping(value = "/{id}", method = GET)
    public ApiResponse getBet(@PathVariable String id) {
        if(id == null || isEmpty(id)){
            throw new BadRequestException("Invalid parameter value");
        }
        Bet bet = betService.getBet(fromString(id));
        return new ApiResponse(asList(bet));
    }

    @RequestMapping(value = "/accept/{id}", method = GET)
    public void acceptBet(@PathVariable String id) {
        if(id == null || isEmpty(id)){
            throw new BadRequestException("Invalid parameter value");
        }
        betService.acceptBet(fromString(id));
    }

    @RequestMapping(value = "/delete/{id}", method = DELETE)
    public void deleteBet(@PathVariable String id) {
        if(id == null || isEmpty(id)){
            throw new BadRequestException("Invalid parameter value");
        }
        betService.deleteBet(fromString(id));
    }

    @RequestMapping(value = "/create", method = POST)
    public ApiResponse createBet(
            @RequestParam String createdBy,
            @RequestParam String createdFor,
            @RequestParam String title,
            @RequestParam String description) {
        if(createdBy == null || createdFor == null || isEmpty(createdBy) || isEmpty(createdFor)){
            throw new BadRequestException("Invalid parameter value");
        }
        return new ApiResponse(asList(betService.create(fromString(createdBy), fromString(createdFor), title, description)));
    }
    
}
