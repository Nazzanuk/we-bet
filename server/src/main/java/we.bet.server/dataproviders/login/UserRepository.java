package we.bet.server.dataproviders.login;

import org.springframework.data.mongodb.repository.MongoRepository;
import we.bet.server.core.domain.login.WeBetUser;

import java.util.UUID;

public interface UserRepository extends MongoRepository<WeBetUser, UUID> {
    WeBetUser findOneByUsername(String username);
}