package we.bet.server.core.usecase.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.User;
import we.bet.server.dataproviders.UserRepository;

public class LoginService {

    private UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public LoginService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean authenticate(String username, String password) {
        User user = repository.findOne(username);

        if(user == null){
            return false;
        }

        if(!encoder.matches(password, user.getPassword())){
            return false;
        }

        return true;
    }

    public boolean register(String username, String password) {
        if(repository.exists(username)){
            return false;
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        repository.save(new User(username, encryptedPassword));
        return true;
    }
}
