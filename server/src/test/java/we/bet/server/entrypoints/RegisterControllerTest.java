package we.bet.server.entrypoints;

import org.junit.Test;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.ConflictException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterControllerTest {
    private WeBetUserService weBetUserService = mock(WeBetUserService.class);
    private RegisterController registerController = new RegisterController(weBetUserService);

    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";

    @Test
    public void returnHttpOkWhenSuccessfulRegister() throws Exception {
        when(weBetUserService.register(USERNAME, PASSWORD)).thenReturn(true);
        registerController.register(USERNAME, PASSWORD);
    }

    @Test(expected = ConflictException.class)
    public void returnHttpConflictWhenUsernameAlreadyExists() throws Exception {
        when(weBetUserService.register(USERNAME, PASSWORD)).thenReturn(false);
        registerController.register(USERNAME, PASSWORD);
    }
}