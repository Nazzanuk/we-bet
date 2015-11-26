package we.bet.server.core.domain.profile;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class WeBetUserProfile {

    @Id
    private UUID userId;
    private String firstname;
    private String lastname;

    public WeBetUserProfile(UUID userId, String firstname, String lastname) {
        this.userId = userId;
        this.firstname = firstname.substring(0, 1).toUpperCase() + firstname.toLowerCase().substring(1);
        this.lastname = lastname.substring(0, 1).toUpperCase() + lastname.toLowerCase().substring(1);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
