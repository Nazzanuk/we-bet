package we.bet.server.core.usecase.bet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import we.bet.server.core.domain.bet.Bet;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.dataproviders.bet.BetRepository;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.entrypoints.PaginatedApiResponse;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.NotFoundException;
import we.bet.server.entrypoints.exceptions.UnauthorizedException;

import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static we.bet.server.core.domain.bet.Bet.Status.ACCEPTED;
import static we.bet.server.core.domain.bet.Bet.Status.CREATED;

public class BetServiceTest {

    private final BetRepository betRepository = mock(BetRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final Bet bet = mock(Bet.class);
    private final BetService betService = new BetService(betRepository, userRepository);
    private final String USERNAME = "someuser";
    private static final String USERNAME2 = "someuser2";
    private static final String TITLE = "someTitle";
    private static final String DESCRIPTION = "someDesc";
    private final WeBetUser weBetUser =new WeBetUser(USERNAME, "pass");
    private UUID id;
    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private final Page page = mock(Page.class);

    @Before
    public void setUp(){
        id = UUID.randomUUID();
    }

    @Test
    public void getAllBetsReturnsEmptyPageContentWhenNoBetsFound(){
        when(betRepository.findByCreatedByUserIdOrCreatedForUserId(eq(uuid1), eq(uuid1), any(Pageable.class))).thenReturn(page);
        when(page.getContent()).thenReturn(emptyList());
        when(page.getTotalElements()).thenReturn(0L);
        when(page.getTotalPages()).thenReturn(0);
        when(page.isLast()).thenReturn(true);
        
        PaginatedApiResponse<Bet> got = betService.getAllBets(uuid1, 0, 1);
        assertThat(got.getContent()).isEmpty();
        assertThat(got.getTotalElements()).isEqualTo(0);
        assertThat(got.getTotalPages()).isEqualTo(0);
        assertThat(got.isLastPage()).isTrue();
    }

    @Test
    public void getAllBetsReturnsPageContentOfBets(){
        Bet bet = new Bet(uuid1, uuid2, "title", "desc");
        when(betRepository.findByCreatedByUserIdOrCreatedForUserId(eq(uuid1), eq(uuid1), any(Pageable.class))).thenReturn(page);
        when(page.getContent()).thenReturn(asList(bet));
        when(page.getTotalElements()).thenReturn(1L);
        when(page.getTotalPages()).thenReturn(1);
        when(page.isLast()).thenReturn(true);
        
        PaginatedApiResponse<Bet> got = betService.getAllBets(uuid1, 0, 1);
        assertThat(got.getContent()).isEqualTo(asList(bet));
        assertThat(got.getTotalElements()).isEqualTo(1);
        assertThat(got.getTotalPages()).isEqualTo(1);
        assertThat(got.isLastPage()).isTrue();
    }

    @Test
    public void getAllBetsReturnsPageContentOfBetsWithMorePages(){
        Bet bet = new Bet(uuid1, uuid2, "title", "desc");
        when(betRepository.findByCreatedByUserIdOrCreatedForUserId(eq(uuid1), eq(uuid1), any(Pageable.class))).thenReturn(page);
        when(page.getContent()).thenReturn(asList(bet));
        when(page.getTotalElements()).thenReturn(2L);
        when(page.getTotalPages()).thenReturn(2);
        when(page.isLast()).thenReturn(false);
        
        PaginatedApiResponse<Bet> got = betService.getAllBets(uuid1, 0, 1);
        assertThat(got.getContent()).isEqualTo(asList(bet));
        assertThat(got.getTotalElements()).isEqualTo(2);
        assertThat(got.getTotalPages()).isEqualTo(2);
        assertThat(got.isLastPage()).isFalse();
    }

    @Test
    public void createSavesAndReturnsNewBet(){
        WeBetUser weBetUser1 = new WeBetUser(USERNAME, "pass");
        WeBetUser weBetUser2 = new WeBetUser(USERNAME2, "pass");
        Bet expectedBet = new Bet(uuid1, uuid2, TITLE, DESCRIPTION);
        when(userRepository.findOne(uuid1)).thenReturn(weBetUser1);
        when(userRepository.findOne(uuid2)).thenReturn(weBetUser2);
        when(betRepository.save(any(Bet.class))).thenReturn(expectedBet);
        Bet got = betService.create(uuid1, uuid2, TITLE, DESCRIPTION);

        verify(betRepository).save(any(Bet.class));
        assertThat(got.getCreatedByUserId()).isEqualTo(uuid1);
        assertThat(got.getCreatedForUserId()).isEqualTo(uuid2);
        assertThat(got.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(got.getTitle()).isEqualTo(TITLE);
        assertThat(got.getStatus()).isEqualTo(CREATED);
        assertThat(got.getId()).isEqualTo(expectedBet.getId());
        assertThat(got.getCreatedDate()).isEqualTo(expectedBet.getCreatedDate());
    }

    @Test(expected = BadRequestException.class)
    public void createThrowsBadRequestExceptionWhenCreatedForDoesNotExist(){
        when(userRepository.findOne(uuid1)).thenReturn(weBetUser);
        when(userRepository.findOne(uuid2)).thenReturn(null);
        try {
            betService.create(uuid1, uuid2, TITLE, DESCRIPTION);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("CreatedFor user not found");
            verifyZeroInteractions(betRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createThrowsBadRequestExceptionWhenCreatedByDoesNotExist(){
        when(userRepository.findOne(uuid1)).thenReturn(null);
        when(userRepository.findOne(uuid2)).thenReturn(weBetUser);
        try {
            betService.create(uuid1, uuid2, TITLE, DESCRIPTION);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("CreatedBy user not found");
            verifyZeroInteractions(betRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createThrowsBadRequestExceptionWhenTitleIsNull(){
        try {
            betService.create(uuid1, uuid2, null, DESCRIPTION);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betRepository, userRepository);
            throw e;
        }    }

    @Test(expected = BadRequestException.class)
    public void createThrowsBadRequestExceptionWhenDescriptionIsNull(){
        try {
            betService.create(uuid1, uuid2, TITLE, null);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
         public void createThrowsBadRequestExceptionWhenCreatedForIsNull(){
        try {
            betService.create(uuid1, null, TITLE, DESCRIPTION);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
         public void createThrowsBadRequestExceptionWhenCreatedByIsNull(){
        try {
            betService.create(null, uuid2, TITLE, DESCRIPTION);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createThrowsBadRequestExceptionWhenTitleIsEmpty(){
        try {
            betService.create(uuid1, uuid2, "", DESCRIPTION);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createThrowsBadRequestExceptionWhenDescriptionIsEmpty(){
        try {
            betService.create(uuid1, uuid2, TITLE, "");
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betRepository, userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedByIsEqualToCreatedFor(){
        try {
            betService.create(uuid1, uuid1, TITLE, DESCRIPTION);
        }
        catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("CreatedBy user cannot be the same as CreatedFor user");
            verifyZeroInteractions(betRepository, userRepository);
            throw e;
        }
    }

    @Test
    public void deleteBetDeletesBetWhenExistsAndIsInCreatedStateAndWasCreatedByUser(){
        when(betRepository.findOne(id)).thenReturn(bet);
        when(bet.getStatus()).thenReturn(CREATED);
        when(bet.getCreatedByUserId()).thenReturn(uuid1);
        betService.deleteBet(uuid1, id);

        verify(betRepository).delete(id);
    }

    @Test(expected = NotFoundException.class)
    public void deleteBetThrowsBadRequestExceptionWhenBetDoesNotExist(){
        when(betRepository.findOne(id)).thenReturn(null);
        try{
            betService.deleteBet(uuid1, id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet not found");
            verify(betRepository, never()).delete(id);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void deleteBetThrowsBadRequestExceptionWhenBetIsNotInCreatedState(){
        when(betRepository.findOne(id)).thenReturn(bet);
        when(bet.getStatus()).thenReturn(ACCEPTED);
        try{
            betService.deleteBet(uuid1, id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet cannot be deleted. Invalid bet state");
            verify(betRepository, never()).delete(id);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void deleteBetThrowsBadRequestExceptionWhenIdIsNull(){
        try{
            betService.deleteBet(uuid1, null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void deleteBetThrowsUnauthorizedExceptionWhenUserDidNotCreateBet(){
        when(betRepository.findOne(id)).thenReturn(bet);
        when(bet.getStatus()).thenReturn(CREATED);
        when(bet.getCreatedByUserId()).thenReturn(uuid2);
        betService.deleteBet(uuid1, id);
    }

    @Test
    public void getBetReturnsBetWhenExists(){
        when(betRepository.findOne(id)).thenReturn(bet);
        Bet got = betService.getBet(id);
        verify(betRepository).findOne(id);
        assertThat(got).isEqualTo(bet);
    }

    @Test(expected = NotFoundException.class)
    public void getBetThrowsBadRequestExceptionWhenBetDoesNotExist(){
        when(betRepository.findOne(id)).thenReturn(null);
        try{
            betService.getBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet not found");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void getBetThrowsBadRequestExceptionWhenIdIsNull(){
        try{
            betService.getBet(null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test
    public void acceptBetUpdatesBetToAcceptedWhenExistsAndIsInCreatedState(){
        when(betRepository.findOne(id)).thenReturn(bet);
        when(bet.getStatus()).thenReturn(CREATED);
        when(bet.getCreatedForUserId()).thenReturn(id);
        betService.acceptBet(id);
        verify(betRepository).save(bet);
        verify(bet).setStatus(ACCEPTED);
    }

    @Test(expected = UnauthorizedException.class)
    public void acceptBetThrowsUnauthorizedExceptionWhenCreatedForIsNotEqual(){
        when(betRepository.findOne(id)).thenReturn(bet);
        when(bet.getStatus()).thenReturn(CREATED);
        when(bet.getCreatedForUserId()).thenReturn(UUID.randomUUID());
        try{
            betService.acceptBet(id);
        } catch (Exception e){
            verify(betRepository, never()).save(any(Bet.class));
            throw e;
        }
    }

    @Test(expected = NotFoundException.class)
    public void acceptBetThrowsBadRequestExceptionWhenBetDoesNotExist(){
        when(betRepository.findOne(id)).thenReturn(null);
        try{
            betService.acceptBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet not found");
            verify(betRepository, never()).save(any(Bet.class));
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void acceptBetThrowsBadRequestExceptionWhenBetIsNotInCreatedState(){
        when(betRepository.findOne(id)).thenReturn(bet);
        when(bet.getStatus()).thenReturn(ACCEPTED);

        try{
            betService.acceptBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet cannot be accepted. Invalid bet state");
            verify(betRepository, never()).save(any(Bet.class));
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void acceptBetThrowsBadRequestExceptionWhenIdIsNull(){
        try{
            betService.acceptBet(null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

}