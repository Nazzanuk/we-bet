package we.bet.server.entrypoints;

import org.junit.Test;
import we.bet.server.core.usecase.login.LoginService;
import we.bet.server.entrypoints.exceptions.UnauthorizedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginService loginService = mock(LoginService.class);
    private LoginController loginController = new LoginController(loginService);

    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";

    @Test
    public void returnHttpOkWhenSuccessfulAuthentication() throws Exception {
        when(loginService.authenticate(USERNAME, PASSWORD)).thenReturn(true);
        loginController.login(USERNAME, PASSWORD);
    }

    @Test(expected = UnauthorizedException.class)
    public void returnHttpUnauthorisedWhenUnSuccessfulAuthentication() throws Exception {
        when(loginService.authenticate(USERNAME, PASSWORD)).thenReturn(false);
        loginController.login(USERNAME, PASSWORD);
    }
}