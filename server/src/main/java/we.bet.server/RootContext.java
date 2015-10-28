package we.bet.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import we.bet.server.core.usecase.login.WeBetUserService;
import we.bet.server.dataproviders.UserRepository;
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
    public LoginController loginController(){
        return new LoginController();
    }

    @Bean
    public RegisterController registerController (WeBetUserService weBetUserService){
        return new RegisterController(weBetUserService);
    }

    @Bean
    public IndexController indexController(){ return new IndexController();}
}
