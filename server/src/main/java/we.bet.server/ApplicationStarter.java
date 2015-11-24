package we.bet.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        RootContext.class,
})
@EnableAutoConfiguration
public class ApplicationStarter {
    
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

}
