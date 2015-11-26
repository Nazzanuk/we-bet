package we.bet.server.dataproviders.friend;

import org.springframework.data.mongodb.repository.MongoRepository;
import we.bet.server.core.domain.friend.Friend;

import java.util.UUID;

public interface FriendRepository extends MongoRepository<Friend, UUID> {
}
