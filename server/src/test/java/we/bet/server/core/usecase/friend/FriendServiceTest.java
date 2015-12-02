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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static we.bet.server.core.domain.friend.FriendRequest.Status.ACCEPTED;
import static we.bet.server.core.domain.friend.FriendRequest.Status.DECLINED;
import static we.bet.server.core.domain.friend.FriendRequest.Status.REQUESTED;

public class FriendServiceTest {

    private final FriendRequestRepository friendRequestRepository = mock(FriendRequestRepository.class);
    private final FriendRepository friendRepository = mock(FriendRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final FriendService friendService = new FriendService(friendRequestRepository, friendRepository, userRepository);
    private final UUID requestBy = UUID.randomUUID();
    private final UUID requestFor = UUID.randomUUID();
    private final UUID friendRequestId = UUID.randomUUID();
    private final WeBetUser requestedByUser = mock(WeBetUser.class);
    private final WeBetUser requestedForUser = mock(WeBetUser.class);
    private final FriendRequest friendRequest = mock(FriendRequest.class);
    private final Friend friend = mock(Friend.class);
    private final Friend friend2 = mock(Friend.class);

    @Test
    public void requestCreatesFriendRequest(){
        when(userRepository.findOne(requestBy)).thenReturn(requestedByUser);
        when(userRepository.findOne(requestFor)).thenReturn(requestedForUser);
        when(friendRequestRepository.findOneByRequestedByUserIdAndRequestedForUserId(requestBy, requestFor)).thenReturn(null);
        when(friendRequestRepository.findOneByRequestedForUserIdAndRequestedByUserId(requestFor, requestBy)).thenReturn(null);
        when(friendRepository.findOne(requestBy)).thenReturn(friend);
        when(friend.getFriendsList()).thenReturn(new HashSet<>());
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
        when(friendRequestRepository.findOneByRequestedByUserIdAndRequestedForUserId(requestBy, requestFor)).thenReturn(friendRequest);
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
        when(friendRequestRepository.findOneByRequestedByUserIdAndRequestedForUserId(requestBy, requestFor)).thenReturn(null);
        when(friendRequestRepository.findOneByRequestedForUserIdAndRequestedByUserId(requestFor, requestBy)).thenReturn(friendRequest);
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
        HashSet<UUID> friendsListSet = new HashSet<>();
        friendsListSet.add(requestFor);
        when(userRepository.findOne(requestBy)).thenReturn(requestedByUser);
        when(userRepository.findOne(requestFor)).thenReturn(requestedForUser);
        when(friendRequestRepository.findOneByRequestedByUserIdAndRequestedForUserId(requestBy, requestFor)).thenReturn(friendRequest);
        when(friendRequestRepository.findOneByRequestedForUserIdAndRequestedByUserId(requestFor, requestBy)).thenReturn(friendRequest);
        when(friendRepository.findOne(requestBy)).thenReturn(friend);
        when(friend.getFriendsList()).thenReturn(friendsListSet);
        try{
            friendService.request(requestBy, requestFor);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Users are already friends");
            verify(friendRequestRepository, never()).save(any(FriendRequest.class));
            throw e;
        }
    }

    @Test
    public void acceptUpdatesFriendRequestToAcceptedAndAddsToExistingFriendsList(){
        Set<UUID> requestForSet = new HashSet<>();
        requestForSet.add(requestBy);
        Set<UUID> requestBySet = new HashSet<>();
        requestBySet.add(requestFor);

        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(friendRequest);
        when(friendRequest.getStatus()).thenReturn(REQUESTED);
        when(friendRequest.getRequestedForUserId()).thenReturn(requestFor);
        when(friendRequest.getRequestedByUserId()).thenReturn(requestBy);
        when(friendRepository.findOne(requestFor)).thenReturn(friend);
        when(friendRepository.findOne(requestBy)).thenReturn(friend2);
        when(friend.getFriendsList()).thenReturn(new HashSet<>());
        when(friend2.getFriendsList()).thenReturn(new HashSet<>());

        friendService.accept(requestFor, friendRequestId);
        verify(friendRequest).setStatus(ACCEPTED);
        verify(friendRequestRepository).save(friendRequest);
        verify(friend).setFriendsList(requestForSet);
        verify(friend2).setFriendsList(requestBySet);
        verify(friendRepository).save(friend);
        verify(friendRepository).save(friend2);
    }

    @Test(expected = BadRequestException.class)
    public void acceptThrowsBadRequestExceptionWhenRequestByUserIdIsNull(){
        try{
            friendService.accept(null, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid value parameter");
            verifyZeroInteractions(friendRepository, friendRequestRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void acceptThrowsBadRequestExceptionWhenFriendRequestIdIsNull(){
        try{
            friendService.accept(requestFor, null);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid value parameter");
            verifyZeroInteractions(friendRepository, friendRequestRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void acceptThrowsBadRequestExceptionWhenFriendRequestNotFound(){
        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(null);
        try{
            friendService.accept(requestFor, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Friend request not found");
            verifyZeroInteractions(friendRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void acceptThrowsBadRequestExceptionWhenFriendRequestNotInRequestedState(){
        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(friendRequest);
        when(friendRequest.getStatus()).thenReturn(ACCEPTED);
        try{
            friendService.accept(requestFor, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid friend request state");
            verifyZeroInteractions(friendRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void acceptThrowsBadRequestExceptionWhenFriendRequestIsNotIntendedForUser(){
        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(friendRequest);
        when(friendRequest.getStatus()).thenReturn(REQUESTED);
        when(friendRequest.getRequestedForUserId()).thenReturn(requestBy);
        try{
            friendService.accept(requestFor, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Friend request is not intended for user");
            verifyZeroInteractions(friendRepository);
            throw e;
        }
    }

    @Test
    public void declineSetsFriendRequestStatusToDeclined(){
        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(friendRequest);
        when(friendRequest.getStatus()).thenReturn(REQUESTED);
        when(friendRequest.getRequestedForUserId()).thenReturn(requestFor);
        when(friendRequest.getRequestedByUserId()).thenReturn(requestBy);

        friendService.decline(requestFor, friendRequestId);
        verify(friendRequest).setStatus(DECLINED);
        verify(friendRequestRepository).save(friendRequest);
    }

    @Test(expected = BadRequestException.class)
    public void declineThrowsBadRequestExceptionWhenRequestByUserIdIsNull(){
        try{
            friendService.decline(null, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid value parameter");
            verifyZeroInteractions(friendRepository, friendRequestRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void declineThrowsBadRequestExceptionWhenFriendRequestIdIsNull(){
        try{
            friendService.decline(requestFor, null);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid value parameter");
            verifyZeroInteractions(friendRepository, friendRequestRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void declineThrowsBadRequestExceptionWhenFriendRequestNotFound(){
        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(null);
        try{
            friendService.decline(requestFor, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Friend request not found");
            verifyZeroInteractions(friendRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void declineThrowsBadRequestExceptionWhenFriendRequestNotInRequestedState(){
        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(friendRequest);
        when(friendRequest.getStatus()).thenReturn(ACCEPTED);
        try{
            friendService.decline(requestFor, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid friend request state");
            verifyZeroInteractions(friendRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void declineThrowsBadRequestExceptionWhenFriendRequestIsNotIntendedForUser(){
        when(friendRequestRepository.findOne(friendRequestId)).thenReturn(friendRequest);
        when(friendRequest.getStatus()).thenReturn(REQUESTED);
        when(friendRequest.getRequestedForUserId()).thenReturn(requestBy);
        try{
            friendService.decline(requestFor, friendRequestId);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Friend request is not intended for user");
            verifyZeroInteractions(friendRepository);
            throw e;
        }
    }

}