package we.bet.server.entrypoints;

import org.junit.Test;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterControllerTest {
    private WeBetUserService weBetUserService = mock(WeBetUserService.class);
    private RegisterController registerController = new RegisterController(weBetUserService);

    private static final String USERNAME = "someUsername@domain.com";
    private static final String BAD_USERNAME = "someUsername.domain.com";
    private static final String PASSWORD = "somePassword";

    @Test
    public void returnHttpOkWhenSuccessfulRegister() throws Exception {
        weBetUserService.register(USERNAME, PASSWORD);
        registerController.register(USERNAME, PASSWORD);
    }

    @Test(expected = ConflictException.class)
    public void returnHttpConflictWhenUsernameAlreadyExists() throws Exception {
        doThrow(new ConflictException()).when(weBetUserService).register(USERNAME, PASSWORD);
        registerController.register(USERNAME, PASSWORD);
    }

    @Test(expected = BadRequestException.class)
    public void returnHttpBadRequestWhenUsernameIsNotAnEmailAddress() throws Exception {
        doThrow(new BadRequestException()).when(weBetUserService).register(BAD_USERNAME, PASSWORD);
        registerController.register(BAD_USERNAME, PASSWORD);
    }
}