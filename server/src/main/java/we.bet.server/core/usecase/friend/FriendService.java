package we.bet.server.core.usecase.friend;

import we.bet.server.core.domain.friend.Friend;
import we.bet.server.core.domain.friend.FriendRequest;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.dataproviders.friend.FriendRepository;
import we.bet.server.dataproviders.friend.FriendRequestRepository;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.util.UUID;

public class FriendService {
    private FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRequestRepository friendRequestRepository,
                         FriendRepository friendRepository,
                         UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public void request(UUID requestedBy, UUID requestedFor) {
        if(requestedBy == null || requestedFor == null){
            throw new BadRequestException("Invalid parameter value");
        }

        if(requestedBy == requestedFor){
            throw new BadRequestException("RequestedBy cannot be the same as RequestedFor");
        }

        WeBetUser requestedByUser = userRepository.findOne(requestedBy);
        if(requestedByUser == null){
            throw new BadRequestException("RequestedBy user does not exist");
        }

        WeBetUser requestedForUser = userRepository.findOne(requestedFor);
        if(requestedForUser == null){
            throw new BadRequestException("RequestedFor user does not exist");
        }

        Friend requestedByFriend = friendRepository.findOne(requestedBy);
        if(requestedByFriend != null && requestedByFriend.getFriendsList().contains(requestedFor)){
            throw new ConflictException("Users are already friends");
        }

        Friend requestedForFriend = friendRepository.findOne(requestedFor);
        if(requestedForFriend != null && requestedForFriend.getFriendsList().contains(requestedBy)){
            throw new ConflictException("Users are already friends");
        }

        FriendRequest requestedByAndRequestedFor = friendRequestRepository.findOneByRequestedByUserIdAndRequestedForUserId(requestedBy, requestedFor);
        FriendRequest requestedForAndRequestedBy = friendRequestRepository.findOneByRequestedForUserIdAndRequestedByUserId(requestedFor, requestedBy);
        if(requestedByAndRequestedFor != null || requestedForAndRequestedBy != null){
            throw new ConflictException("A friend request already exists");
        }

        FriendRequest friendRequest = new FriendRequest(requestedBy, requestedFor);
        friendRequestRepository.save(friendRequest);
    }
}
