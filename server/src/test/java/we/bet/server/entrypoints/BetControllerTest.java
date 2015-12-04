package we.bet.server.entrypoints;

import org.junit.Before;
import org.junit.Test;
import we.bet.server.core.domain.bet.Bet;
import we.bet.server.core.usecase.bet.BetService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.representation.ApiResponse;
import we.bet.server.entrypoints.representation.PaginatedApiResponse;

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
    private String betId;
    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();

    @Before
    public void setUp(){
        expectedBet = new Bet(uuid1, uuid2, TITLE, DESCRIPTION);
        betId = UUID.randomUUID().toString();
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
        verify(weBetUserService).getIdForUser("USER");
    }

    @Test
    public void getAllBetsReturnsEmptyBetsWhenNoBetsReturned(){
        PaginatedApiResponse<Bet> betPaginatedApiResponse = new PaginatedApiResponse<>(emptyList(), 0L, 1, true);
        when(betService.getAllBets(uuid1, 0, 2)).thenReturn(betPaginatedApiResponse);
        PaginatedApiResponse<Bet> got = betController.getAllBets(0, 2, principal);
        assertThat(got).isEqualTo(betPaginatedApiResponse);
        verify(weBetUserService).getIdForUser("USER");
    }

    @Test
    public void createBetReturnsBetOnSuccessfulCreate(){
        when(betService.create(uuid1, uuid2, TITLE, DESCRIPTION)).thenReturn(expectedBet);
        ApiResponse got = betController.createBet(uuid2.toString(), TITLE, DESCRIPTION, principal);
        assertThat(got.getContent()).isEqualTo(asList(expectedBet));
        verify(weBetUserService).getIdForUser("USER");
    }

    @Test
    public void deleteBetReturnsHttpOkWhenBetDeleted(){
        betController.deleteBet(betId, principal);
        verify(betService).deleteBet(uuid1, fromString(betId));
        verify(weBetUserService).getIdForUser("USER");
    }

    @Test
    public void getBetReturnsHttpOkWhenBetReturned(){
        when(betService.getBet(uuid1, fromString(betId))).thenReturn(bet);
        ApiResponse got = betController.getBet(betId, principal);
        verify(betService).getBet(uuid1, fromString(betId));
        assertThat(got.getContent().get(0)).isEqualTo(bet);
        verify(weBetUserService).getIdForUser("USER");
    }

    @Test
    public void acceptBetReturnsHttpOkWhenBetAccepted(){
        betController.acceptBet(betId, principal);
        verify(betService).acceptBet(uuid1, fromString(betId));
        verify(weBetUserService).getIdForUser("USER");
    }

}