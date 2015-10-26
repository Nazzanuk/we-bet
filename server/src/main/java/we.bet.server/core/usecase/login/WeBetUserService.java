package we.bet.server.core.usecase.login;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.WeBetUser;
import we.bet.server.dataproviders.UserRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

public class WeBetUserService {

    private UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final EmailValidator emailValidator = new EmailValidator();

    public WeBetUserService(UserRepository repository) {
        this.repository = repository;
    }

    public WeBetUser register(String username, String password) {
        if(!emailValidator.isValid(username, null)){
            throw new BadRequestException();
        }

        if(repository.exists(username)){
            throw new ConflictException();
        }

        String encryptedPassword = encoder.encode(password);
        return repository.save(new WeBetUser(username, encryptedPassword));
    }
}
