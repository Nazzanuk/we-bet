package we.bet.server.dataproviders;

import org.springframework.data.mongodb.repository.MongoRepository;
import we.bet.server.core.domain.WeBetUser;

public interface UserRepository extends MongoRepository<WeBetUser, String> {
}