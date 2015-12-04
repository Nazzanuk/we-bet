package we.bet.server.entrypoints;

import org.junit.Before;
import org.junit.Test;
import we.bet.server.core.domain.profile.WeBetUserProfile;
import we.bet.server.core.usecase.friend.FriendService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.representation.ApiResponse;
import we.bet.server.entrypoints.representation.BasicWeBetUserProfileRepresentation;

import java.security.Principal;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FriendControllerTest {

    private final FriendService friendService = mock(FriendService.class);
    private final WeBetUserService weBetUserService = mock(WeBetUserService.class);
    private final FriendController friendController = new FriendController(friendService, weBetUserService);
    private final UUID requestBy = UUID.randomUUID();
    private final UUID requestFor = UUID.randomUUID();
    private final UUID friendRequestId = UUID.randomUUID();
    private final Principal principal = mock(Principal.class);
    private final WeBetUserProfile weBetUserProfile = mock(WeBetUserProfile.class);

    @Before
    public void beforeStep(){
        when(principal.getName()).thenReturn("USER");
        when(weBetUserService.getIdForUser("USER")).thenReturn(requestBy);
    }
    
    @Test
    public void requestCreatesFriendRequest(){
        friendController.friendRequest(requestFor.toString(), principal);
        verify(weBetUserService).getIdForUser("USER");
        verify(friendService).request(requestBy, requestFor);
    }

    @Test
    public void acceptFriendRequest(){
        friendController.acceptRequest(friendRequestId.toString(), principal);
        verify(weBetUserService).getIdForUser("USER");
        verify(friendService).accept(requestBy, friendRequestId);
    }

    @Test
    public void declineFriendRequest(){
        friendController.declineRequest(friendRequestId.toString(), principal);
        verify(weBetUserService).getIdForUser("USER");
        verify(friendService).decline(requestBy, friendRequestId);
    }

    @Test
    public void getFriendListRequestReturnsListOfBasicFriendRepresentation(){
        WeBetUserProfile weBetUserProfile2 = new WeBetUserProfile(requestFor, "uncle", "bob");
        BasicWeBetUserProfileRepresentation basicWeBetUserProfileRepresentation = new BasicWeBetUserProfileRepresentation(weBetUserProfile2);
        when(friendService.getFriendsList(requestBy)).thenReturn(asList(weBetUserProfile));
        when(weBetUserProfile.getUserId()).thenReturn(requestFor);
        when(weBetUserProfile.getFirstname()).thenReturn("Uncle");
        when(weBetUserProfile.getLastname()).thenReturn("Bob");
        ApiResponse response = friendController.getFriendsList(principal);
        assertThat(response.getContent()).isEqualTo(asList(basicWeBetUserProfileRepresentation));
    }

    @Test
    public void getFriendListRequestReturnsEmptyListOfBasicFriendRepresentationWhenUserHasNoFriends(){
        when(friendService.getFriendsList(requestBy)).thenReturn(asList());
        ApiResponse response = friendController.getFriendsList(principal);
        assertThat(response.getContent()).isEqualTo(asList());
    }

    @Test
    public void getFriendRequestReturnsFriend(){
        when(friendService.getFriend(requestBy, requestFor)).thenReturn(weBetUserProfile);
        ApiResponse friend = friendController.getFriend(requestFor.toString(), principal);
        assertThat(friend.getContent()).isEqualTo(asList(weBetUserProfile));
    }

}