package we.bet.server.dataproviders;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import we.bet.server.core.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
}