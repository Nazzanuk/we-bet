package we.bet.server.dataproviders.bet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import we.bet.server.ApplicationStarter;
import we.bet.server.core.domain.bet.Bet;

import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationStarter.class)
public class BetRepositoryTest {

    @Autowired
    public BetRepository betRepository;

    @Before
    public void beforeStep(){
        betRepository.deleteAll();
    }

    @Test
    public void findByCreatedByOrCreatedForReturnsNoPagesWhenNoResults(){
        UUID id = UUID.randomUUID();
        PageRequest pageRequest = new PageRequest(
                0,
                1,
                new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        Page<Bet> got = betRepository.findByCreatedByUserIdOrCreatedForUserId(id, id, pageRequest);

        assertThat(got.getTotalElements()).isEqualTo(0);
        assertThat(got.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findByCreatedByOrCreatedForReturnsSinglePageTotalElementsIsLessThanPageSize(){
        UUID createdBy = UUID.randomUUID();
        UUID createdFor = UUID.randomUUID();

        Bet bet = new Bet(createdBy, createdFor, "title", "desc");
        betRepository.save(bet);

        PageRequest pageRequest = new PageRequest(
                0,
                2,
                new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        Page<Bet> got = betRepository.findByCreatedByUserIdOrCreatedForUserId(createdBy, createdBy, pageRequest);

        assertThat(got.getTotalElements()).isEqualTo(1);
        assertThat(got.getTotalPages()).isEqualTo(1);
        assertThat(got.hasNext()).isFalse();
    }

    @Test
    public void findByCreatedByOrCreatedForReturnsSinglePageTotalElementsIsMoreThanPageSize(){
        UUID createdFor = UUID.randomUUID();
        UUID createdBy = UUID.randomUUID();

        Bet bet = new Bet(createdFor, createdBy, "title", "desc");
        Bet bet2 = new Bet(createdBy, createdFor, "title", "desc");
        betRepository.save(bet);
        betRepository.save(bet2);

        PageRequest pageRequest = new PageRequest(
                0,
                1,
                new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        Page<Bet> got = betRepository.findByCreatedByUserIdOrCreatedForUserId(createdFor, createdFor, pageRequest);

        assertThat(got.getTotalElements()).isEqualTo(2);
        assertThat(got.getTotalPages()).isEqualTo(2);
        assertThat(got.hasNext()).isTrue();
    }

}