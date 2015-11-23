package we.bet.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import we.bet.server.core.usecase.bet.BetService;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.dataproviders.bet.BetRepository;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.entrypoints.BetController;
import we.bet.server.entrypoints.IndexController;
import we.bet.server.entrypoints.LoginController;
import we.bet.server.entrypoints.RegisterController;

@Configuration
@Import(value = {
        MongoDbContext.class,
        SpringSecurityContext.class
})
public class RootContext {
    @Bean
    public WeBetUserService loginService(UserRepository userRepository){
        return new WeBetUserService(userRepository);
    }

    @Bean
    public BetService betService(BetRepository betRepository, UserRepository userRepository){
        return new BetService(betRepository, userRepository);
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
}
