package we.bet.server.core.usecase.login;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.User;
import we.bet.server.dataproviders.UserRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository = mock(UserRepository.class);
    private final LoginService loginService = new LoginService(userRepository);
    private static final String USERNAME = "someUser";
    private static final String PASSWORD = "somePassword";
    private static final String WRONG_PASSWORD = "someWrongPassword";

    @Test
    public void authenticateReturnsFalseWhenUserNotFound(){
        when(userRepository.findOne(USERNAME)).thenReturn(null);
        boolean got = loginService.authenticate(USERNAME, PASSWORD);

        assertThat(got).isFalse();
    }

    @Test
    public void authenticateReturnsFalseWhenPasswordDoesNotMatch(){
        when(userRepository.findOne(USERNAME)).thenReturn(user());
        boolean got = loginService.authenticate(USERNAME, WRONG_PASSWORD);

        assertThat(got).isFalse();
    }

    @Test
    public void authenticateReturnsTrueWhenUsernameAndPasswordMatch(){
        when(userRepository.findOne(USERNAME)).thenReturn(user());
        boolean got = loginService.authenticate(USERNAME, PASSWORD);

        assertThat(got).isTrue();
    }

    @Test
    public void registerReturnsFalseWhenUsernameExists(){
        when(userRepository.exists(USERNAME)).thenReturn(true);
        boolean got = loginService.register(USERNAME, PASSWORD);

        assertThat(got).isFalse();
    }

    @Test
    public void registerReturnsTrueWhenUsernameDoesNotExist(){
        when(userRepository.exists(USERNAME)).thenReturn(false);
        boolean got = loginService.register(USERNAME, PASSWORD);

        assertThat(got).isTrue();
        verify(userRepository).save(any(User.class));
    }

    private User user(){
        return new User(USERNAME, encoder.encode(PASSWORD));
    }
}