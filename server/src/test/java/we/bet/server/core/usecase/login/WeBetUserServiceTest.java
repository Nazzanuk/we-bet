package we.bet.server.core.usecase.login;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.WeBetUser;
import we.bet.server.dataproviders.UserRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class WeBetUserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final WeBetUserService weBetUserService = new WeBetUserService(userRepository);
    private static final String USERNAME = "someUser@somedomain.com";
    private static final String BAD_USERNAME = "someUser.somedomain.com";
    private static final String PASSWORD = "somePassword";

    @Test(expected = ConflictException.class)
    public void registerThrowsConflictExceptionWhenUsernameExists(){
        when(userRepository.exists(USERNAME)).thenReturn(true);
        weBetUserService.register(USERNAME, PASSWORD);
        verify(userRepository, never()).save(any(WeBetUser.class));
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenUsernameIsNotValid(){
        weBetUserService.register(BAD_USERNAME, PASSWORD);
        verifyZeroInteractions(userRepository);
    }

    @Test
    public void registerReturnsSuccessfullyWhenUsernameDoesNotExistAndUsernameIsValid(){
        when(userRepository.exists(USERNAME)).thenReturn(false);
        weBetUserService.register(USERNAME, PASSWORD);
        verify(userRepository).save(any(WeBetUser.class));
    }

    @Test
    public void registerSavesUsernameInLowerCase(){
        String upperCaseUsername = USERNAME.toUpperCase();
        when(userRepository.save(any(WeBetUser.class))).thenReturn(new WeBetUser(upperCaseUsername, PASSWORD));
        when(userRepository.exists(USERNAME)).thenReturn(false);
        WeBetUser got = weBetUserService.register(upperCaseUsername, PASSWORD);
        verify(userRepository).save(any(WeBetUser.class));
        assertThat(got.getUsername()).isEqualTo(USERNAME.toLowerCase());
    }

}