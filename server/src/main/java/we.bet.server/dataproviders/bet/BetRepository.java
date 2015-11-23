package we.bet.server.dataproviders.bet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import we.bet.server.core.domain.bet.Bet;

import java.util.UUID;

public interface BetRepository extends MongoRepository<Bet, UUID> {

        Page<Bet> findByCreatedByOrCreatedFor(UUID createdBy, UUID createdFor, Pageable pageable);

}