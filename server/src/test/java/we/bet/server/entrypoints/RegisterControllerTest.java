package we.bet.server.entrypoints;

import org.junit.Test;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {
    private WeBetUserService weBetUserService = mock(WeBetUserService.class);
    private RegisterController registerController = new RegisterController(weBetUserService);

    private static final String USERNAME = "someUsername@domain.com";
    private static final String BAD_USERNAME = "someUsername.domain.com";
    private static final String PASSWORD = "somePassword";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    @Test
    public void returnApiResponseWithUUIDAndHttpOkWhenSuccessfulRegister() throws Exception {
        UUID uuid = UUID.randomUUID();
        Map map = new HashMap<>();
        map.put("userId", uuid);
        when(weBetUserService.register(USERNAME, PASSWORD, FIRSTNAME, LASTNAME)).thenReturn(uuid);
        ApiResponse<UUID> register = registerController.register(USERNAME, PASSWORD, FIRSTNAME, LASTNAME);
        assertThat(register.getContent()).isEqualTo(asList(map));
    }

}