package we.bet.server;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        RootContext.class,
})
@EnableAutoConfiguration
public class Application {
    
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

//    @Bean
//    public LoginService loginService(UserRepository userRepository){
//        return new LoginService(userRepository);
//    }
//
//    @Bean
//    public LoginController loginController (LoginService loginService){
//        return new LoginController(loginService);
//    }

}
