package we.bet.server.dataproviders.profile;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import we.bet.server.ApplicationStarter;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.core.domain.profile.WeBetUserProfile;
import we.bet.server.dataproviders.login.UserRepository;

import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationStarter.class)
public class ProfileRepositoryTest {

    @Autowired
    public ProfileRepository profileRepository;

    @Before
    public void beforeStep(){
        profileRepository.deleteAll();
    }

    @Test
    public void firstnameIsStoredWithFirstLetterCapitalised(){
        WeBetUserProfile weBetUserProfile = new WeBetUserProfile(UUID.randomUUID(), "firstNAME", "Lastname");
        WeBetUserProfile got = profileRepository.save(weBetUserProfile);
        assertThat(got.getFirstname()).isEqualTo("Firstname");
    }

    @Test
    public void lastnameIsStoredWithFirstLetterCapitalised(){
        WeBetUserProfile weBetUserProfile = new WeBetUserProfile(UUID.randomUUID(), "Firstname", "lastNAME");
        WeBetUserProfile got = profileRepository.save(weBetUserProfile);
        assertThat(got.getLastname()).isEqualTo("Lastname");
    }


}