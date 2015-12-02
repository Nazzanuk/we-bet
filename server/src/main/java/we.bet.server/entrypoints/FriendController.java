package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.usecase.friend.FriendService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;

import java.security.Principal;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;
    private final WeBetUserService weBetUserService;

    public FriendController(FriendService friendService, WeBetUserService weBetUserService) {
        this.friendService = friendService;
        this.weBetUserService = weBetUserService;
    }

    @RequestMapping(value = "/request", method = POST)
    public void friendRequest(
            @RequestParam String requestFor,
            Principal principal) {
        UUID requestByUserId = weBetUserService.getIdForUser(principal.getName());
        friendService.request(requestByUserId, fromString(requestFor));
    }

    @RequestMapping(value = "/accept/{friendRequestId}", method = POST)
    public void acceptRequest(
            @PathVariable String friendRequestId,
            Principal principal) {
        UUID requestByUserId = weBetUserService.getIdForUser(principal.getName());
        friendService.accept(requestByUserId, fromString(friendRequestId));
    }



}
