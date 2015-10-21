package we.bet.server.core.usecase.login;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.WeBetUser;
import we.bet.server.dataproviders.UserRepository;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeBetUserServiceTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository = mock(UserRepository.class);
    private final WeBetUserService weBetUserService = new WeBetUserService(userRepository);
    private static final String USERNAME = "someUser";
    private static final String PASSWORD = "somePassword";

    @Test
    public void registerReturnsFalseWhenUsernameExists(){
        when(userRepository.exists(USERNAME)).thenReturn(true);
        boolean got = weBetUserService.register(USERNAME, PASSWORD);

        assertThat(got).isFalse();
    }

    @Test
    public void registerReturnsTrueWhenUsernameDoesNotExist(){
        when(userRepository.exists(USERNAME)).thenReturn(false);
        boolean got = weBetUserService.register(USERNAME, PASSWORD);

        assertThat(got).isTrue();
        verify(userRepository).save(any(WeBetUser.class));
    }

}