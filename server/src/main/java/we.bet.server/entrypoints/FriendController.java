package we.bet.server.entrypoints;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import we.bet.server.core.domain.profile.WeBetUserProfile;
import we.bet.server.core.usecase.friend.FriendService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.representation.ApiResponse;
import we.bet.server.entrypoints.representation.BasicWeBetUserProfileRepresentation;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
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

    @RequestMapping(value = "/decline/{friendRequestId}", method = POST)
    public void declineRequest(
            @PathVariable String friendRequestId,
            Principal principal) {
        UUID requestByUserId = weBetUserService.getIdForUser(principal.getName());
        friendService.decline(requestByUserId, fromString(friendRequestId));
    }

    @RequestMapping(value = "/list", method = GET)
    public ApiResponse getFriendsList(
            Principal principal) {
        UUID requestByUserId = weBetUserService.getIdForUser(principal.getName());
        List<WeBetUserProfile> friendsList = friendService.getFriendsList(requestByUserId);
        List<BasicWeBetUserProfileRepresentation> basicFriendsList = friendsList.stream()
                .map(weBetUserProfile -> new BasicWeBetUserProfileRepresentation(weBetUserProfile))
                .collect(Collectors.toList());
        return new ApiResponse(basicFriendsList);
    }

    @RequestMapping(value = "/get/{friendUserId}", method = GET)
    public ApiResponse getFriend(
            @PathVariable String friendUserId,
            Principal principal) {
        UUID requestByUserId = weBetUserService.getIdForUser(principal.getName());
        WeBetUserProfile friend = friendService.getFriend(requestByUserId, fromString(friendUserId));
        return new ApiResponse(asList(friend));
    }

}
