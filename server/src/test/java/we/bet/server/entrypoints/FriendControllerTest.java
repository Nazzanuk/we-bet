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
    private final Principal principal = mock(Principal.class);

    @Before
    public void beforeStep(){
        when(principal.getName()).thenReturn("USER");
        when(weBetUserService.getIdForUser("USER")).thenReturn(requestBy);
    }
    
    @Test
    public void requestCreatesFriendRequest(){
        friendController.friendRequest(requestFor.toString(), principal);
        verify(friendService).request(requestBy, requestFor);
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedByIsNull(){
        when(principal.getName()).thenReturn(null);
        try{
            friendController.friendRequest(requestFor.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(friendService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedByIsEmpty(){
        when(principal.getName()).thenReturn("");
        try{
            friendController.friendRequest(requestFor.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(friendService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedForIsNull(){
        try{
            friendController.friendRequest(null, principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(friendService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedForIsEmpty(){
        try{
            friendController.friendRequest("", principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(friendService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedByIsEqualToRequestedFor(){
        doThrow(new BadRequestException("RequestedBy cannot be the same as RequestedFor")).when(friendService).request(requestBy, requestBy);
        try{
            friendController.friendRequest(requestBy.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("RequestedBy cannot be the same as RequestedFor");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedByDoesNotExist(){
        doThrow(new BadRequestException("RequestedBy user does not exist")).when(friendService).request(requestBy, requestFor);

        try{
            friendController.friendRequest(requestFor.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("RequestedBy user does not exist");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedForDoesNotExist(){
        doThrow(new BadRequestException("RequestedFor user does not exist")).when(friendService).request(requestBy, requestFor);
        try{
            friendController.friendRequest(requestFor.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("RequestedFor user does not exist");
            throw e;
        }
    }

    @Test(expected = ConflictException.class)
    public void requestThrowsConflictExceptionWhenFriendRequestAlreadyExists(){
        doThrow(new ConflictException("A friend request already exists")).when(friendService).request(requestBy, requestFor);
        try{
            friendController.friendRequest(requestFor.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("A friend request already exists");
            throw e;
        }
    }

    @Test(expected = ConflictException.class)
    public void requestThrowsConflictExceptionWhenAReverseFriendRequestAlreadyExists(){
        doThrow(new ConflictException("A friend request already exists")).when(friendService).request(requestBy, requestFor);
        try{
            friendController.friendRequest(requestFor.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("A friend request already exists");
            throw e;
        }
    }

    @Test(expected = ConflictException.class)
    public void requestThrowsConflictExceptionWhenExistingFriends(){
        doThrow(new ConflictException("Users are already friends")).when(friendService).request(requestBy, requestFor);
        try{
            friendController.friendRequest(requestFor.toString(), principal);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Users are already friends");
            throw e;
        }
    }

}