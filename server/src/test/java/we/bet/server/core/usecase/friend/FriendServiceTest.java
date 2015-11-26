package we.bet.server.core.usecase.friend;

import org.junit.Test;
import we.bet.server.core.domain.friend.Friend;
import we.bet.server.core.domain.friend.FriendRequest;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.dataproviders.friend.FriendRepository;
import we.bet.server.dataproviders.friend.FriendRequestRepository;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.util.Collections;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FriendServiceTest {

    private final FriendRequestRepository friendRequestRepository = mock(FriendRequestRepository.class);
    private final FriendRepository friendRepository = mock(FriendRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final FriendService friendService = new FriendService(friendRequestRepository, friendRepository, userRepository);
    private final UUID requestBy = UUID.randomUUID();
    private final UUID requestFor = UUID.randomUUID();
    private final WeBetUser requestedByUser = mock(WeBetUser.class);
    private final WeBetUser requestedForUser = mock(WeBetUser.class);
    private final FriendRequest friendRequest = mock(FriendRequest.class);
    private final Friend friend = mock(Friend.class);

    @Test
    public void requestCreatesFriendRequest(){
        when(userRepository.findOne(requestBy)).thenReturn(requestedByUser);
        when(userRepository.findOne(requestFor)).thenReturn(requestedForUser);
        when(friendRequestRepository.findOneByRequestedByAndRequestedFor(requestBy, requestFor)).thenReturn(null);
        when(friendRequestRepository.findOneByRequestedForAndRequestedBy(requestFor, requestBy)).thenReturn(null);
        when(friendRepository.findOne(requestBy)).thenReturn(friend);
        when(friend.getFriendsList()).thenReturn(Collections.<UUID>emptyList());
        friendService.request(requestBy, requestFor);

        verify(friendRequestRepository).save(new FriendRequest(requestBy, requestFor));
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedByIsNull(){
        try{
            friendService.request(null, requestFor);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(friendRequestRepository, friendRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedForIsNull(){
        try{
            friendService.request(requestBy, null);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(friendRequestRepository, friendRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedByIsEqualToRequestedFor(){
        try{
            friendService.request(requestBy, requestBy);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("RequestedBy cannot be the same as RequestedFor");
            verifyZeroInteractions(friendRequestRepository, friendRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedByDoesNotExist(){
        when(userRepository.findOne(requestBy)).thenReturn(null);
        when(userRepository.findOne(requestFor)).thenReturn(requestedForUser);
        try{
            friendService.request(requestBy, requestFor);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("RequestedBy user does not exist");
            verifyZeroInteractions(friendRequestRepository, friendRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void requestThrowsBadRequestExceptionWhenRequestedForDoesNotExist(){
        when(userRepository.findOne(requestBy)).thenReturn(requestedByUser);
        when(userRepository.findOne(requestFor)).thenReturn(null);
        try{
            friendService.request(requestBy, requestFor);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("RequestedFor user does not exist");
            verifyZeroInteractions(friendRequestRepository, friendRepository);
            throw e;
        }
    }

    @Test(expected = ConflictException.class)
    public void requestThrowsConflictExceptionWhenFriendRequestAlreadyExists(){
        when(userRepository.findOne(requestBy)).thenReturn(requestedByUser);
        when(userRepository.findOne(requestFor)).thenReturn(requestedForUser);
        when(friendRequestRepository.findOneByRequestedByAndRequestedFor(requestBy, requestFor)).thenReturn(friendRequest);
        try{
            friendService.request(requestBy, requestFor);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("A friend request already exists");
            verify(friendRequestRepository, never()).save(any(FriendRequest.class));
            throw e;
        }
    }

    @Test(expected = ConflictException.class)
    public void requestThrowsConflictExceptionWhenAReverseFriendRequestAlreadyExists(){
        when(userRepository.findOne(requestBy)).thenReturn(requestedByUser);
        when(userRepository.findOne(requestFor)).thenReturn(requestedForUser);
        when(friendRequestRepository.findOneByRequestedByAndRequestedFor(requestBy, requestFor)).thenReturn(null);
        when(friendRequestRepository.findOneByRequestedForAndRequestedBy(requestFor, requestBy)).thenReturn(friendRequest);
        try{
            friendService.request(requestBy, requestFor);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("A friend request already exists");
            verify(friendRequestRepository, never()).save(any(FriendRequest.class));
            throw e;
        }
    }

    @Test(expected = ConflictException.class)
    public void requestThrowsConflictExceptionWhenExistingFriends(){
        when(userRepository.findOne(requestBy)).thenReturn(requestedByUser);
        when(userRepository.findOne(requestFor)).thenReturn(requestedForUser);
        when(friendRequestRepository.findOneByRequestedByAndRequestedFor(requestBy, requestFor)).thenReturn(friendRequest);
        when(friendRequestRepository.findOneByRequestedForAndRequestedBy(requestFor, requestBy)).thenReturn(friendRequest);
        when(friendRepository.findOne(requestBy)).thenReturn(friend);
        when(friend.getFriendsList()).thenReturn(asList(requestFor));
        try{
            friendService.request(requestBy, requestFor);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Users are already friends");
            verify(friendRequestRepository, never()).save(any(FriendRequest.class));
            throw e;
        }
    }

}