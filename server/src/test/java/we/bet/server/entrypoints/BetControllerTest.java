package we.bet.server.entrypoints;

import org.junit.Before;
import org.junit.Test;
import we.bet.server.core.domain.bet.Bet;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.core.usecase.bet.BetService;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.NotFoundException;

import java.security.Principal;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BetControllerTest {

    private final BetService betService = mock(BetService.class);
    private final BetController betController = new BetController(betService);
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
    }

    @Test
    public void getAllBetsReturnsBetsWhenBetsFound(){
        Bet bet1 = new Bet(uuid1, uuid2, TITLE, DESCRIPTION);
        Bet bet2 = new Bet(uuid1, uuid2, TITLE2, DESCRIPTION2);
        PaginatedApiResponse<Bet> betPaginatedApiResponse = new PaginatedApiResponse<>(asList(bet1, bet2), 2L, 1, true);
        when(betService.getAllBets(uuid1, 0, 2)).thenReturn(betPaginatedApiResponse);
        PaginatedApiResponse<Bet> got = betController.getAllBets(0, 2, uuid1.toString());
        assertThat(got).isEqualTo(betPaginatedApiResponse);
    }

    @Test
    public void getAllBetsReturnsEmptyBetsWhenNoBetsReturned(){
        PaginatedApiResponse<Bet> betPaginatedApiResponse = new PaginatedApiResponse<>(emptyList(), 0L, 1, true);
        when(betService.getAllBets(uuid1, 0, 2)).thenReturn(betPaginatedApiResponse);
        PaginatedApiResponse<Bet> got = betController.getAllBets(0, 2, uuid1.toString());
        assertThat(got).isEqualTo(betPaginatedApiResponse);
    }

    @Test(expected = BadRequestException.class)
    public void getAllBetsThrowsBadRequestExceptionWhenIdIsNull(){
        try{
            betController.getAllBets(0, 1, null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void getAllBetsThrowsBadRequestExceptionWhenIdIsEmpty(){
        try{
            betController.getAllBets(0, 1, "");
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test
    public void createBetReturnsBetOnSuccessfulCreate(){
        when(betService.create(uuid1, uuid2, TITLE, DESCRIPTION)).thenReturn(expectedBet);
        ApiResponse got = betController.createBet(uuid1.toString(), uuid2.toString(), TITLE, DESCRIPTION);
        assertThat(got.getContent()).isEqualTo(asList(expectedBet));
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedForUserNotFound(){
        when(betService.create(uuid1, uuid2, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("CreatedFor user not found"));
        try{
            betController.createBet(uuid1.toString(), uuid2.toString(), TITLE, DESCRIPTION);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("CreatedFor user not found");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedForIsNull() {
        when(betService.create(uuid1, null, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid1.toString(), null, TITLE, DESCRIPTION);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedByIsNull() {
        when(betService.create(null, uuid2, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(null, uuid2.toString(), TITLE, DESCRIPTION);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedForIsEmpty() {
        try{
            betController.createBet(uuid1.toString(), "", TITLE, DESCRIPTION);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedByIsEmpty() {
        try{
            betController.createBet("", uuid2.toString(), TITLE, DESCRIPTION);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(betService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenTitleIsNull() {
        when(betService.create(uuid1, uuid2, null, DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid1.toString(), uuid2.toString(), null, DESCRIPTION);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenTitleIsEmpty() {
        when(betService.create(uuid1, uuid2, "", DESCRIPTION)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid1.toString(), uuid2.toString(), "", DESCRIPTION);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenDescriptionIsNull() {
        when(betService.create(uuid1, uuid2, TITLE, null)).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid1.toString(), uuid2.toString(), TITLE, null);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenDescriptionIsEmpty() {
        when(betService.create(uuid1, uuid2, TITLE, "")).thenThrow(new BadRequestException("Invalid parameter value"));
        try{
            betController.createBet(uuid1.toString(), uuid2.toString(), TITLE, "");
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void createBetThrowsBadRequestExceptionWhenCreatedByIsEqualToCreatedFor(){
        when(betService.create(uuid1, uuid1, TITLE, DESCRIPTION)).thenThrow(new BadRequestException("CreatedBy user cannot be the same as CreatedFor user"));
        try{
            betController.createBet(uuid1.toString(), uuid1.toString(), TITLE, DESCRIPTION);
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

}