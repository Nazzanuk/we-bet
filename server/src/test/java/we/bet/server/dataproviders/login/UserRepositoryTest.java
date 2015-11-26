package we.bet.server.dataproviders.login;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import we.bet.server.ApplicationStarter;
import we.bet.server.core.domain.login.WeBetUser;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationStarter.class)
public class UserRepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @Before
    public void beforeStep(){
        userRepository.deleteAll();
    }

    @Test
    public void canFindOneByUsername(){
        WeBetUser user = createUser();
        WeBetUser got = userRepository.findOneByUsername(user.getUsername());
        assertThat(got).isEqualTo(user);
    }

    @Test
    public void usernameIsStoredInLowercase(){
        WeBetUser weBetUser = new WeBetUser("USER", "password");
        WeBetUser save = userRepository.save(weBetUser);
        WeBetUser got = userRepository.findOne(save.getId());
        assertThat(got.getUsername()).isEqualTo("user");
    }

    private WeBetUser createUser() {
        WeBetUser weBetUser = new WeBetUser("user", "password");
        return userRepository.save(weBetUser);
    }


}