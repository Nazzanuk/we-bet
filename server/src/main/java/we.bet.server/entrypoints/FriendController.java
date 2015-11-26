package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.usecase.friend.FriendService;
import we.bet.server.entrypoints.exceptions.BadRequestException;

import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @RequestMapping(value = "/request", method = POST)
    public void friendRequest(
            @RequestParam String requestBy,
            @RequestParam String requestFor
    ) {
        if(isEmpty(requestBy) || isEmpty(requestFor)){
            throw new BadRequestException("Invalid parameter value");
        }
        friendService.request(fromString(requestBy), fromString(requestFor));
    }

}
