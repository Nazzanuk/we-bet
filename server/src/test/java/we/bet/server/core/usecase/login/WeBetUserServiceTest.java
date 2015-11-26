package we.bet.server.core.usecase.login;

import org.junit.Test;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.core.domain.profile.WeBetUserProfile;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.dataproviders.profile.ProfileRepository;
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
    private final ProfileRepository profileRepository = mock(ProfileRepository.class);
    private final WeBetUserService weBetUserService = new WeBetUserService(userRepository, profileRepository);
    private static final String USERNAME = "someUser@somedomain.com";
    private static final String BAD_USERNAME = "someUser.somedomain.com";
    private static final String PASSWORD = "somePassword";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    @Test(expected = ConflictException.class)
    public void registerThrowsConflictExceptionWhenUsernameExists(){
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(weBetUser);
        try{
            weBetUserService.register(USERNAME, PASSWORD, FIRSTNAME, LASTNAME);
        }catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Username already exists");
            verify(userRepository, never()).save(any(WeBetUser.class));
            verifyZeroInteractions(profileRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenUsernameIsNotValid(){
        try{
            weBetUserService.register(BAD_USERNAME, PASSWORD, FIRSTNAME, LASTNAME);
        } catch (Exception e){
            assertThat(e.getMessage()).isEqualTo("Username is not valid format");
            verifyZeroInteractions(userRepository, profileRepository);
            throw e;
        }
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenUsernameIsNull(){
        weBetUserService.register(null, PASSWORD, FIRSTNAME, LASTNAME);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenUsernameIsEmpty(){
        weBetUserService.register("", PASSWORD, FIRSTNAME, LASTNAME);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenPasswordIsNull(){
        weBetUserService.register(USERNAME, null, FIRSTNAME, LASTNAME);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenPasswordIsEmpty(){
        weBetUserService.register(USERNAME, "", FIRSTNAME, LASTNAME);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenFirstnameIsNull(){
        weBetUserService.register(USERNAME, PASSWORD, null, LASTNAME);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenFirstnameIsEmpty(){
        weBetUserService.register(USERNAME, PASSWORD, "", LASTNAME);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenLastnameIsNull(){
        weBetUserService.register(USERNAME, PASSWORD, FIRSTNAME, null);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void registerThrowsBadRequestExceptionWhenLastnameIsEmpty(){
        weBetUserService.register(USERNAME, PASSWORD, FIRSTNAME, "");
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test
    public void registerReturnsUUIDWhenUsernameDoesNotExistAndUsernameIsValidAndCreatedProfileEntry(){
        String upperCaseUsername = USERNAME.toUpperCase();
        WeBetUser weBetUser = new WeBetUser(upperCaseUsername, PASSWORD);
        when(userRepository.save(any(WeBetUser.class))).thenReturn(weBetUser);
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(null);
        UUID got = weBetUserService.register(upperCaseUsername, PASSWORD, FIRSTNAME, LASTNAME);
        verify(userRepository).save(any(WeBetUser.class));
        verify(profileRepository).save(any(WeBetUserProfile.class));
        assertThat(got).isEqualTo(weBetUser.getUserId());
    }

    @Test
    public void getIdForUserReturnsOptionalWithUUIDWhenUserUserExists(){
        UUID uuid = UUID.randomUUID();
        when(weBetUser.getUserId()).thenReturn(uuid);
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(weBetUser);
        Optional<UUID> got = weBetUserService.getIdForUser(USERNAME);
        assertThat(got.isPresent()).isTrue();
        assertThat(got.get()).isEqualTo(uuid);
        verify(userRepository, never()).save(any(WeBetUser.class));
        verifyZeroInteractions(profileRepository);
    }

    @Test
    public void getIdForUserReturnsEmptyOptionalWhenUserNotExists(){
        when(weBetUser.getUserId()).thenReturn(null);
        when(userRepository.findOneByUsername(USERNAME)).thenReturn(null);
        Optional<UUID> got = weBetUserService.getIdForUser(USERNAME);
        assertThat(got.isPresent()).isFalse();
        verify(userRepository, never()).save(any(WeBetUser.class));
        verifyZeroInteractions(profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void getIdForUserThrowsBadRequestExceptionWhenUsernameIsNull(){
        weBetUserService.getIdForUser(null);
        verifyZeroInteractions(userRepository, profileRepository);
    }

    @Test(expected = BadRequestException.class)
    public void getIdForUserThrowsBadRequestExceptionWhenUsernameIsEmpty(){
        weBetUserService.getIdForUser("");
        verifyZeroInteractions(userRepository, profileRepository);
    }

}