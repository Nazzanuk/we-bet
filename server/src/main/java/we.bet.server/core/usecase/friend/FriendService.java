package we.bet.server.core.usecase.friend;

import we.bet.server.core.domain.friend.Friend;
import we.bet.server.core.domain.friend.FriendRequest;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.core.domain.profile.WeBetUserProfile;
import we.bet.server.dataproviders.friend.FriendRepository;
import we.bet.server.dataproviders.friend.FriendRequestRepository;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.dataproviders.profile.ProfileRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static we.bet.server.core.domain.friend.FriendRequest.Status.ACCEPTED;
import static we.bet.server.core.domain.friend.FriendRequest.Status.DECLINED;
import static we.bet.server.core.domain.friend.FriendRequest.Status.REQUESTED;

public class FriendService {
    private FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private ProfileRepository profileRepository;

    public FriendService(FriendRequestRepository friendRequestRepository,
                         FriendRepository friendRepository,
                         UserRepository userRepository,
                         ProfileRepository profileRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
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

    public void accept(UUID requestForUserId, UUID friendRequestId) {
        FriendRequest friendRequest = validateFriendRequest(requestForUserId, friendRequestId);
        friendRequest.setStatus(ACCEPTED);

        UUID requestedByUserId = friendRequest.getRequestedByUserId();
        Friend requestedByFriend = friendRepository.findOne(requestedByUserId);
        Set<UUID> requestedByFriendFriendsList = requestedByFriend.getFriendsList();
        requestedByFriendFriendsList.add(requestForUserId);
        requestedByFriend.setFriendsList(requestedByFriendFriendsList);

        Friend requestedForFriend = friendRepository.findOne(requestForUserId);
        Set<UUID> requestedForFriendFriendsList = requestedForFriend.getFriendsList();
        requestedForFriendFriendsList.add(requestedByUserId);
        requestedForFriend.setFriendsList(requestedForFriendFriendsList);

        friendRepository.save(requestedByFriend);
        friendRepository.save(requestedForFriend);
        friendRequestRepository.save(friendRequest);
    }

    public void decline(UUID requestForUserId, UUID friendRequestId) {
        FriendRequest friendRequest = validateFriendRequest(requestForUserId, friendRequestId);
        friendRequest.setStatus(DECLINED);
        friendRequestRepository.save(friendRequest);
    }

    private FriendRequest validateFriendRequest(UUID requestForUserId, UUID friendRequestId) {
        if(requestForUserId == null || friendRequestId == null){
            throw new BadRequestException("Invalid value parameter");
        }

        FriendRequest friendRequest = friendRequestRepository.findOne(friendRequestId);
        if(friendRequest == null){
            throw new BadRequestException("Friend request not found");
        }
        if(friendRequest.getStatus() != REQUESTED){
            throw new BadRequestException("Invalid friend request state");
        }
        if(friendRequest.getRequestedForUserId() != requestForUserId){
            throw new BadRequestException("Friend request is not intended for user");
        }
        return friendRequest;
    }

    public List<WeBetUserProfile> getFriendsList(UUID userId) {
        if(userId == null){
            throw new BadRequestException("Invalid value parameter");
        }
        Friend friend = friendRepository.findOne(userId);
        Set<UUID> friendsList = friend.getFriendsList();
        return friendsList.stream()
                .map(friendId -> profileRepository.findOne(friendId))
                .filter(profile -> profile != null)
                .collect(Collectors.toList());
    }

    public WeBetUserProfile getFriend(UUID requestByUserId, UUID friendUserId) {
        if(requestByUserId == null || friendUserId == null){
            throw new BadRequestException("Invalid value parameter");
        }

        Friend friend = friendRepository.findOne(requestByUserId);
        Set<UUID> friendsList = friend.getFriendsList();
        if(!friendsList.contains(friendUserId)){
            throw new BadRequestException("Users are not friends");
        }

        WeBetUserProfile friendProfile = profileRepository.findOne(friendUserId);
        if(friendProfile == null){
            throw new BadRequestException("Friend profile not found");
        }

        return friendProfile;
    }
}
