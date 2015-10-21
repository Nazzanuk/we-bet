package we.bet.server.core.usecase.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.WeBetUser;
import we.bet.server.dataproviders.UserRepository;

public class WeBetUserService {

    private UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public WeBetUserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean register(String username, String password) {
        if(repository.exists(username)){
            return false;
        }

        String encryptedPassword = encoder.encode(password);
        repository.save(new WeBetUser(username, encryptedPassword));
        return true;
    }
}
