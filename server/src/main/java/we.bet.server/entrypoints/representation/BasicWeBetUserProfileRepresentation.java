package we.bet.server.entrypoints.representation;

import we.bet.server.core.domain.profile.WeBetUserProfile;

import java.util.UUID;

public class BasicWeBetUserProfileRepresentation {

    private final UUID id;
    private final String firstname;
    private final String lastname;

    public BasicWeBetUserProfileRepresentation(WeBetUserProfile weBetUserProfile){
        this.id = weBetUserProfile.getUserId();
        this.firstname = weBetUserProfile.getFirstname();
        this.lastname = weBetUserProfile.getLastname();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicWeBetUserProfileRepresentation that = (BasicWeBetUserProfileRepresentation) o;

        if (!id.equals(that.id)) return false;
        if (!firstname.equals(that.firstname)) return false;
        return lastname.equals(that.lastname);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstname.hashCode();
        result = 31 * result + lastname.hashCode();
        return result;
    }
}
