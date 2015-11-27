package we.bet.server.core.usecase.bet;

import org.omg.CORBA.UnknownUserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import we.bet.server.core.domain.bet.Bet;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.dataproviders.bet.BetRepository;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.entrypoints.PaginatedApiResponse;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.NotFoundException;
import we.bet.server.entrypoints.exceptions.UnauthorizedException;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static we.bet.server.core.domain.bet.Bet.Status.ACCEPTED;
import static we.bet.server.core.domain.bet.Bet.Status.CREATED;

public class BetService {

    private final BetRepository betRepository;
    private final UserRepository userRepository;

    public BetService(BetRepository betRepository, UserRepository userRepository) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
    }

    public PaginatedApiResponse<Bet> getAllBets(UUID id, int page, int pageSize) {
        PageRequest pageRequest = new PageRequest(
                page,
                pageSize,
                new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        Page<Bet> bets = betRepository.findByCreatedByUserIdOrCreatedForUserId(id, id, pageRequest);
        return new PaginatedApiResponse<>(bets.getContent(), bets.getTotalElements(), bets.getTotalPages(), bets.isLast());
    }

    public Bet create(UUID createdBy, UUID createdFor, String title, String description) {
        if( createdBy == null || createdFor == null || isEmpty(title) || isEmpty(description)){
            throw new BadRequestException("Invalid parameter value");
        }

        if(createdBy.equals(createdFor)){
            throw new BadRequestException("CreatedBy user cannot be the same as CreatedFor user");
        }

        WeBetUser createdForUser = userRepository.findOne(createdFor);
        WeBetUser createdByUser = userRepository.findOne(createdBy);
        if(createdForUser == null){
            throw new BadRequestException("CreatedFor user not found");
        }

        if(createdByUser == null){
            throw new BadRequestException("CreatedBy user not found");
        }

        return betRepository.save(new Bet(createdByUser.getUserId(), createdForUser.getUserId(), title, description));
    }

    public void deleteBet(UUID userId, UUID betId) {
        if(betId == null){
            throw new BadRequestException("Invalid parameter value");
        }

        Bet bet = betRepository.findOne(betId);
        if(bet == null){
            throw new NotFoundException("Bet not found");
        }
        if(bet.getStatus() != CREATED){
            throw new BadRequestException("Bet cannot be deleted. Invalid bet state");
        }
        if(bet.getCreatedByUserId() != userId){
            throw new UnauthorizedException();
        }

        betRepository.delete(betId);
    }

    public Bet getBet(UUID userId, UUID betId) {
        if(userId == null || betId == null){
            throw new BadRequestException("Invalid parameter value");
        }

        Bet bet = betRepository.findOne(betId);
        if(bet == null){
            throw new NotFoundException("Bet not found");
        }
        if(bet.getCreatedByUserId() != userId && bet.getCreatedForUserId() != userId){
            throw new UnauthorizedException();
        }

        return bet;
    }

    public void acceptBet(UUID userId, UUID betId) {
        if(userId == null || betId == null){
            throw new BadRequestException("Invalid parameter value");
        }

        Bet bet = betRepository.findOne(betId);
        if(bet == null){
            throw new NotFoundException("Bet not found");
        }

        if(bet.getStatus() != CREATED){
            throw new BadRequestException("Bet cannot be accepted. Invalid bet state");
        }

        if(bet.getCreatedForUserId() != userId){
            throw new UnauthorizedException();
        }

        bet.setStatus(ACCEPTED);
        betRepository.save(bet);
    }
}
