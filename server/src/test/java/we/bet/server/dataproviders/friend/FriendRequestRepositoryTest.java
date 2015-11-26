package we.bet.server.dataproviders.friend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import we.bet.server.ApplicationStarter;
import we.bet.server.core.domain.friend.FriendRequest;

import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationStarter.class)
public class FriendRequestRepositoryTest {

    @Autowired
    public FriendRequestRepository friendRequestRepository;

    private final UUID requestBy = UUID.randomUUID();
    private final UUID requestFor = UUID.randomUUID();

    @Before
    public void beforeStep(){
        friendRequestRepository.deleteAll();
    }

    @Test
    public void canFindOneByRequestedByAndRequestedFor(){
        FriendRequest friendRequest = new FriendRequest(requestBy, requestFor);
        friendRequestRepository.save(friendRequest);
        FriendRequest got = friendRequestRepository.findOneByRequestedByUserIdAndRequestedForUserId(requestBy, requestFor);
        assertThat(got).isEqualTo(friendRequest);
    }

    @Test
    public void canFindOneByRequestedForAndRequestedBy(){
        FriendRequest friendRequest = new FriendRequest(requestBy, requestFor);
        friendRequestRepository.save(friendRequest);
        FriendRequest got = friendRequestRepository.findOneByRequestedForUserIdAndRequestedByUserId(requestFor, requestBy);
        assertThat(got).isEqualTo(friendRequest);
    }
}
