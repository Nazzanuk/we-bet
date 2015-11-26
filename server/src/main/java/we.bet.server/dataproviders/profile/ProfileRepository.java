package we.bet.server.dataproviders.profile;

import org.springframework.data.mongodb.repository.MongoRepository;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.core.domain.profile.WeBetUserProfile;

import java.util.UUID;

public interface ProfileRepository extends MongoRepository<WeBetUserProfile, UUID> {
}