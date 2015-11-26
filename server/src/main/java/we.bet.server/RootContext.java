package we.bet.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import we.bet.server.core.usecase.bet.BetService;
import we.bet.server.core.usecase.friend.FriendService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.dataproviders.bet.BetRepository;
import we.bet.server.dataproviders.friend.FriendRepository;
import we.bet.server.dataproviders.friend.FriendRequestRepository;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.dataproviders.profile.ProfileRepository;
import we.bet.server.entrypoints.*;

@Configuration
@Import(value = {
        MongoDbContext.class,
        SpringSecurityContext.class
})
public class RootContext {
    @Bean
    public WeBetUserService loginService(UserRepository userRepository, ProfileRepository profileRepository){
        return new WeBetUserService(userRepository, profileRepository);
    }

    @Bean
    public BetService betService(BetRepository betRepository, UserRepository userRepository){
        return new BetService(betRepository, userRepository);
    }

    @Bean
    public FriendService friendService(FriendRequestRepository friendRequestRepository,
                                       FriendRepository friendRepository,
                                       UserRepository userRepository){
        return new FriendService(friendRequestRepository, friendRepository, userRepository);
    }

    @Bean
    public LoginController loginController(WeBetUserService weBetUserService){
        return new LoginController(weBetUserService);
    }

    @Bean
    public RegisterController registerController (WeBetUserService weBetUserService){
        return new RegisterController(weBetUserService);
    }

    @Bean
    public IndexController indexController(){ return new IndexController();}

    @Bean
    public BetController betController(BetService betService){ return new BetController(betService );}

    @Bean
    public FriendController friendController(FriendService friendService){
        return new FriendController(friendService);
    }
}
