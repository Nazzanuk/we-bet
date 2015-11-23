package we.bet.server.core.usecase.login;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class WeBetUserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final EmailValidator emailValidator = new EmailValidator();

    public WeBetUserService(UserRepository repository) {
        this.repository = repository;
    }

    public UUID register(String username, String password) {
        if( isEmpty(username) || isEmpty(password)){
            throw new BadRequestException("Invalid parameter value");
        }

        if(!emailValidator.isValid(username, null)){
            throw new BadRequestException("Username is not valid format");
        }

        if(repository.findOneByUsername(username) != null){
            throw new ConflictException("Username already exists");
        }

        String encryptedPassword = encoder.encode(password);
        return repository.save(new WeBetUser(username, encryptedPassword)).getId();
    }

    public Optional<UUID> getIdForUser(String username){
        if( isEmpty(username)){
            throw new BadRequestException("Invalid parameter value");
        }

        WeBetUser user = repository.findOneByUsername(username);
        if(user == null){
            return Optional.empty();
        }
        return Optional.of(user.getId());
    }
}
