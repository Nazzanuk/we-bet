package we.bet.server.entrypoints;

import org.junit.Before;
import org.junit.Test;
import we.bet.server.core.usecase.friend.FriendService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.security.Principal;
import java.util.UUID;

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

}