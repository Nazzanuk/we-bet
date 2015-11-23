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

    @Test
    public void returnApiResponseWithUUIDAndHttpOkWhenSuccessfulRegister() throws Exception {
        UUID uuid = UUID.randomUUID();
        Map map = new HashMap<>();
        map.put("id", uuid);
        when(weBetUserService.register(USERNAME, PASSWORD)).thenReturn(uuid);
        ApiResponse<UUID> register = registerController.register(USERNAME, PASSWORD);
        assertThat(register.getContent()).isEqualTo(asList(map));
    }

    @Test(expected = ConflictException.class)
    public void returnHttpConflictWhenUsernameAlreadyExists() throws Exception {
        doThrow(new ConflictException("Username already exists")).when(weBetUserService).register(USERNAME, PASSWORD);
        try{
            registerController.register(USERNAME, PASSWORD);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Username already exists");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void returnHttpBadRequestWhenUsernameIsNotAnEmailAddress() throws Exception {
        doThrow(new BadRequestException("Username is not valid format")).when(weBetUserService).register(BAD_USERNAME, PASSWORD);
        try{
            registerController.register(BAD_USERNAME, PASSWORD);
        } catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Username is not valid format");
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void returnHttpBadRequestWhenUsernameIsNull() throws Exception {
        try{
            registerController.register(null, PASSWORD);
        }catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void returnHttpBadRequestWhenUsernameIsEmpty() throws Exception {
        try{
            registerController.register("", PASSWORD);
        }catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void returnHttpBadRequestWhenPasswordIsNull() throws Exception {
        try{
            registerController.register(USERNAME, null);
        }catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(weBetUserService);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void returnHttpBadRequestWhenPasswordIsEmpty() throws Exception {
        try{
            registerController.register(USERNAME, "");
        }catch(Exception e){
            assertThat(e.getMessage()).isEqualTo("Invalid parameter value");
            verifyZeroInteractions(weBetUserService);
            throw e;
        }
    }
}