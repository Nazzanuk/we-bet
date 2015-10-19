package we.bet.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import we.bet.server.core.usecase.login.LoginService;
import we.bet.server.dataproviders.UserRepository;
import we.bet.server.entrypoints.LoginController;
import we.bet.server.entrypoints.RegisterController;

@Configuration
@Import(value = {
        MongoDbContext.class,
        SpringSecurityContext.class
})
public class RootContext {
    @Bean
    public LoginService loginService(UserRepository userRepository){
        return new LoginService(userRepository);
    }

    @Bean
    public LoginController loginController (LoginService loginService){
        return new LoginController(loginService);
    }

    @Bean
    public RegisterController registerController (LoginService loginService){
        return new RegisterController(loginService);
    }
}
