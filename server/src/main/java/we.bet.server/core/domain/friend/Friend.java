package we.bet.server.core.domain.friend;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.EMPTY_SET;
import static java.util.Collections.emptyList;

public class Friend {

    @Id
    private UUID userId;
    private Set<UUID> friendsList;

    public Friend(UUID userId) {
        this.userId = userId;
        this.friendsList = new HashSet<>();
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<UUID> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(Set<UUID> friendsList) {
        this.friendsList = friendsList;
    }
}
