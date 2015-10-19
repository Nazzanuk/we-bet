package we.bet.server.entrypoints;

import org.junit.Test;
import we.bet.server.core.usecase.login.LoginService;
import we.bet.server.entrypoints.exceptions.ConflictException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterControllerTest {
    private LoginService loginService = mock(LoginService.class);
    private RegisterController registerController = new RegisterController(loginService);

    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";

    @Test
    public void returnHttpOkWhenSuccessfulRegister() throws Exception {
        when(loginService.register(USERNAME, PASSWORD)).thenReturn(true);
        registerController.register(USERNAME, PASSWORD);
    }

    @Test(expected = ConflictException.class)
    public void returnHttpConflictWhenUsernameAlreadyExists() throws Exception {
        when(loginService.register(USERNAME, PASSWORD)).thenReturn(false);
        registerController.register(USERNAME, PASSWORD);
    }
}