package we.bet.server.entrypoints;

import org.junit.Before;
import org.junit.Test;
import we.bet.server.core.domain.bet.Bet;
import we.bet.server.core.usecase.bet.BetService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.NotFoundException;
import we.bet.server.entrypoints.exceptions.UnauthorizedException;

import java.security.Principal;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BetControllerTest {

    private final BetService betService = mock(BetService.class);
    private final Bet bet = mock(Bet.class);
    private final Principal principal = mock(Principal.class);
    private final WeBetUserService weBetUserService = mock(WeBetUserService.class);
    private final BetController betController = new BetController(betService, weBetUserService);
    private static final String TITLE = "someTitle";
    private static final String DESCRIPTION = "someDescription";
    private static final String TITLE2 = "someTitle2";
    private static final String DESCRIPTION2 = "someDescription2";
    private Bet expectedBet;
    private String id;
    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();

    @Before
    public void setUp(){
        expectedBet = new Bet(uuid1, uuid2, TITLE, DESCRIPTION);
        id = UUID.randomUUID().toString();
        when(principal.getName()).thenReturn("USER");
        when(weBetUserService.getIdForUser("USER")).thenReturn(uuid1);
    }

    @Test
    public void getAllBetsReturnsBetsWhenBetsFound(){
        Bet bet1 = new Bet(uuid1, uuid2, TITLE, DESCRIPTION);
        Bet bet2 = new Bet(uuid1, uuid2, TITLE2, DESCRIPTION2);
        PaginatedApiResponse<Bet> betPaginatedApiResponse = new PaginatedApiResponse<>(asList(bet1, bet2), 2L, 1, true);
        when(betService.getAllBets(uuid1, 0, 2)).thenReturn(betPaginatedApiResponse);
        PaginatedApiResponse<Bet> got = betController.getAllBets(0, 2, principal);
        assertThat(got).isEqualTo(betPaginatedApiResponse);
    }

    @Test
    public void getAllBetsReturnsEmptyBetsWhenNoBetsReturned(){
        PaginatedApiResponse<Bet> betPaginatedApiResponse = new PaginatedApiResponse<>(emptyList(), 0L, 1, true);
        when(betService.getAllBets(uuid1, 0, 2)).thenReturn(betPaginatedApiResponse);
        PaginatedApiResponse<Bet> got = betController.getAllBets(0, 2, principal);
        assertThat(got).isEqualTo(betPaginatedApiResponse);
    }

    @Test(expected = BadRequestException.class)
    public void getAllBetsThrowsBadRequestExceptionWhenPrincipalNameIsNull(){
        when(principal.getName()).thenReturn(null);
        try{
            betController.getAllBets(0, 1, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void getAllBetsThrowsBadRequestExceptionWhenPrincipalNameIsEmpty(){
        when(principal.getName()).thenReturn("");
        try{
            betController.getAllBets(0, 1, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService, weBetUserService);
            throw e;
        }
    }

    @Test
    public void createBetReturnsBetOnSuccessfulCreate(){
        when(betService.create(uuid1, uuid2, TITLE, DESCRIPTION)).thenReturn(expectedBet);
        ApiResponse got = betController.createBet(uuid2.toString(), TITLE, DESCRIPTION, principal);
        assertThat(got.getContent()).isEqualTo(asList(expectedBet));
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedForUserNotFound(){
        when(betService.create(uuid1, uuid2, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("CreatedFor user not found"));
        try{
            betController.createBet(uuid2.toString(), TITLE, DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("CreatedFor user not found");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedForIsNull() {
        when(betService.create(uuid1, null, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(null, TITLE, DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenPrincipleNameIsNull() {
        when(principal.getName()).thenReturn(null);
        when(betService.create(null, uuid2, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid2.toString(), TITLE, DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedForIsEmpty() {
        try{
            betController.createBet("", TITLE, DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenPrincipalNameIsEmpty() {
        when(principal.getName()).thenReturn("");
        try{
            betController.createBet(uuid2.toString(), TITLE, DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService, weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenTitleIsNull() {
        when(betService.create(uuid1, uuid2, null, DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid2.toString(), null, DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenTitleIsEmpty() {
        when(betService.create(uuid1, uuid2, "", DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid2.toString(), "", DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenDescriptionIsNull() {
        when(betService.create(uuid1, uuid2, TITLE, null)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid2.toString(), TITLE, null, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenDescriptionIsEmpty() {
        when(betService.create(uuid1, uuid2, TITLE, "")).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid2.toString(), TITLE, "", principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedByIsEqualToCreatedFor(){
        when(betService.create(uuid1, uuid1, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("CreatedBy user cannot be the same as CreatedFor user"));
        try{
            betController.createBet(uuid1.toString(), TITLE, DESCRIPTION, principal);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("CreatedBy user cannot be the same as CreatedFor user");
            throw e;
        }
    }

    @Test
    public void deleteBetReturnsHttpOkWhenBetDeleted(){
        betController.deleteBet(id);
        verify(betService).deleteBet(fromString(id));
    }

    @Test(expected = NotFoundException.class)
    public void deleteBetThrowsBadRequestExceptionWhenBetNotFound(){
        doThrow(new NotFoundException("Bet not found")).when(betService).deleteBet(fromString(id));
        try{
            betController.deleteBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet not found");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void deleteBetThrowsBadRequestExceptionWhenBetNotInCreatedState(){
        doThrow(new BadRequestException("Bet cannot be deleted. Invalid bet state")).when(betService).deleteBet(fromString(id));
        try{
            betController.deleteBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet cannot be deleted. Invalid bet state");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void deleteBetThrowsBadRequestExceptionWhenIdIsNull(){
        try{
            betController.deleteBet(null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void deleteBetThrowsBadRequestExceptionWhenIdIsEmpty(){
        try{
            betController.deleteBet("");
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test
    public void getBetReturnsHttpOkWhenBetReturned(){
        when(betService.getBet(fromString(id))).thenReturn(bet);
        ApiResponse got = betController.getBet(id);
        verify(betService).getBet(fromString(id));
        assertThat(got.getContent().get(0)).isEqualTo(bet);
    }

    @Test(expected = NotFoundException.class)
    public void getBetThrowsNotFoundExceptionWhenBetNotFound(){
        when(betService.getBet(fromString(id))).thenThrow(new NotFoundException("Bet not found"));
        try{
            betController.getBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet not found");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void getBetThrowsBadRequestExceptionWhenIdIsNull(){
        try{
            betController.getBet(null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void getBetThrowsBadRequestExceptionWhenIdIsEmpty(){
        try{
            betController.getBet("");
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test
    public void acceptBetReturnsHttpOkWhenBetAccepted(){
        betController.acceptBet(id);
        verify(betService).acceptBet(fromString(id));
    }

    @Test(expected = BadRequestException.class)
    public void acceptBetThrowsBadRequestExceptionWhenBetNotInCreatedState(){
        doThrow(new BadRequestException("Bet cannot be accepted. Invalid bet state")).when(betService).acceptBet(fromString(id));
        try{
            betController.acceptBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet cannot be accepted. Invalid bet state");
            throw e;
        }
    }

    @Test(expected = NotFoundException.class)
    public void acceptBetThrowsNotFoundExceptionWhenBetNotFound(){
        doThrow(new NotFoundException("Bet not found")).when(betService).acceptBet(fromString(id));
        try{
            betController.acceptBet(id);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Bet not found");
            throw e;
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void acceptBetThrowsUnauthorizedExceptionWhenIdIsNotEqualToCreatedForUser(){
        doThrow(new UnauthorizedException()).when(betService).acceptBet(fromString(id));
        betController.acceptBet(id);
    }

    @Test(expected = BadRequestException.class)
    public void acceptBetThrowsBadRequestExceptionWhenIdIsNull(){
        try{
            betController.acceptBet(null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void acceptBetThrowsBadRequestExceptionWhenIdIsEmpty(){
        try{
            betController.acceptBet("");
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

}