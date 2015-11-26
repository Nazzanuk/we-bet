package we.bet.server.core.usecase.login;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.bet.server.core.domain.login.WeBetUser;
import we.bet.server.core.domain.profile.WeBetUserProfile;
import we.bet.server.dataproviders.login.UserRepository;
import we.bet.server.dataproviders.profile.ProfileRepository;
import we.bet.server.entrypoints.exceptions.BadRequestException;
import we.bet.server.entrypoints.exceptions.ConflictException;

import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class WeBetUserService {

    private final UserRepository repository;
    private ProfileRepository profileRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final EmailValidator emailValidator = new EmailValidator();

    public WeBetUserService(UserRepository repository, ProfileRepository profileRepository) {
        this.repository = repository;
        this.profileRepository = profileRepository;
    }

    public UUID register(String username, String password, String firstname, String lastname) {
        if( isEmpty(username) || isEmpty(password) || isEmpty(firstname) || isEmpty(lastname)){
            throw new BadRequestException("Invalid parameter value");
        }

        if(!emailValidator.isValid(username, null)){
            throw new BadRequestException("Username is not valid format");
        }

        if(repository.findOneByUsername(username) != null){
            throw new ConflictException("Username already exists");
        }

        String encryptedPassword = encoder.encode(password);
        UUID userId = repository.save(new WeBetUser(username, encryptedPassword)).getUserId();
        profileRepository.save(new WeBetUserProfile(userId, firstname, lastname));
        return userId;
    }

    public Optional<UUID> getIdForUser(String username){
        if( isEmpty(username)){
            throw new BadRequestException("Invalid parameter value");
        }

        WeBetUser user = repository.findOneByUsername(username);
        if(user == null){
            return Optional.empty();
        }
        return Optional.of(user.getUserId());
    }
}
