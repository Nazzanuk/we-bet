package we.bet.server.dataproviders.friend;

import org.springframework.data.mongodb.repository.MongoRepository;
import we.bet.server.core.domain.friend.FriendRequest;

import java.util.UUID;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, UUID> {
    FriendRequest findOneByRequestedByAndRequestedFor(UUID requestedBy, UUID requestedFor);

    FriendRequest findOneByRequestedForAndRequestedBy(UUID requestedFor, UUID requestedBy);
}
