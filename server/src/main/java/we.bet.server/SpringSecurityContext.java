package we.bet.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.WeBetUser;
import we.bet.server.dataproviders.UserRepository;

import java.util.Collections;

@Configuration
@EnableWebMvcSecurity
public class SpringSecurityContext extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(new ApiUserDetailsService(userRepository));

        http
            .authorizeRequests()
                .regexMatchers("/register.*").permitAll()
                .anyRequest().authenticated()
                .and()
            .authenticationProvider(authenticationProvider)
                .httpBasic()
                .and()
            .logout()
                .permitAll();

        http.csrf().disable();
    }

    private class ApiUserDetailsService implements UserDetailsService {
        private UserRepository userRepository;

        public ApiUserDetailsService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            final WeBetUser weBetUser = userRepository.findOne(username);
            if(weBetUser == null){
                throw new UsernameNotFoundException("Username not found: " + username);
            }
            return new User(weBetUser.getUsername(), weBetUser.getPassword(), Collections.emptyList());
        }
    }
}
