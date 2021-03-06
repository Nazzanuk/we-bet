package we.bet.server.entrypoints;

import org.junit.Before;
import org.junit.Test;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.representation.ApiResponse;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    private final WeBetUserService weBetUserService = mock(WeBetUserService.class);
    private final LoginController loginController = new LoginController(weBetUserService);
    private final Principal principal = mock(Principal.class);

    @Before
    public void beforeStep(){
        when(principal.getName()).thenReturn("USER");
    }

    @Test
    public void loginReturnsUUIDWhenUserExists(){
        UUID uuid = UUID.randomUUID();
        Map map = new HashMap<>();
        map.put("id", uuid);
        when(weBetUserService.getIdForUser("USER")).thenReturn(uuid);
        ApiResponse got = loginController.login(principal);
        assertThat(got.getContent()).isEqualTo(asList(map));
        verify(weBetUserService).getIdForUser("USER");
    }

}