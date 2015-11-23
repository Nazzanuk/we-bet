package we.bet.server.core.usecase.login;

import org.junit.Test;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.util.Optional;
import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class WeBetUserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final WeBetUser weBetUser = mock(WeBetUser.class);
    private final WeBetUserService weBetUserService = new WeBetUserService(userRepository);
    private static final String USERNAME = "someUser@somedomain.com";
    private static final String BAD_USERNAME = "someUser.somedomain.com";
    private static final String PASSWORD = "somePassword";

    @Test(expected = ConflictException.class)
    public void registerThrowsConflictExceptionWhenUsernameExists(){
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(weBetUser);
        try{
            weBetUserService.register(USERNAME, PASSWORD);
        }catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Username already exists");
            verify(userRepository, never()).save(any(WeBetUser.class));
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenUsernameIsNotValid(){
        try{
            weBetUserService.register(BAD_USERNAME, PASSWORD);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Username is not valid format");
            verifyZeroInteractions(userRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenUsernameIsNull(){
        weBetUserService.register(null, PASSWORD);
        verifyZeroInteractions(userRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenUsernameIsEmpty(){
        weBetUserService.register("", PASSWORD);
        verifyZeroInteractions(userRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenPasswordIsNull(){
        weBetUserService.register(USERNAME, null);
        verifyZeroInteractions(userRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenPasswordIsEmpty(){
        weBetUserService.register(USERNAME, "");
        verifyZeroInteractions(userRepository);
    }

    @Test
    public void registerReturnsUUIDWhenUsernameDoesNotExistAndUsernameIsValid(){
        String upperCaseUsername = USERNAME.toUpperCase();
        WeBetUser weBetUser = new WeBetUser(upperCaseUsername, PASSWORD);
        when(userRepository.save(any(WeBetUser.class))).thenReturn(weBetUser);
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(null);
        UUID got = weBetUserService.register(upperCaseUsername, PASSWORD);
        verify(userRepository).save(any(WeBetUser.class));
        assertThat(got).isEqualTo(weBetUser.getId());
    }

    @Test
    public void getIdForUserReturnsOptionalWithUUIDWhenUserUserExists(){
        UUID uuid = UUID.randomUUID();
        when(weBetUser.getId()).thenReturn(uuid);
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(weBetUser);
        Optional<UUID> got = weBetUserService.getIdForUser(USERNAME);
        assertThat(got.isPresent()).isTrue();
        assertThat(got.get()).isEqualTo(uuid);
    }

    @Test
    public void getIdForUserReturnsEmptyOptionalWhenUserNotExists(){
        when(weBetUser.getId()).thenReturn(null);
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(null);
        Optional<UUID> got = weBetUserService.getIdForUser(USERNAME);
        assertThat(got.isPresent()).isFalse();
    }

    @Test(expected = BadRequestException.class)
    public void getIdForUserThrowsBadRequestExceptionWhenUsernameIsNull(){
        weBetUserService.getIdForUser(null);
    }

    @Test(expected = BadRequestException.class)
    public void getIdForUserThrowsBadRequestExceptionWhenUsernameIsEmpty(){
        weBetUserService.getIdForUser("");
    }

}