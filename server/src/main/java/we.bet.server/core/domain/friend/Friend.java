package we.bet.server.core.domain.friend;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;

public class Friend {

    @Id
    private UUID userId;
    private List<UUID> friendsList;

    public Friend(UUID userId) {
        this.userId = userId;
        this.friendsList = emptyList();
    }

    public UUID getUserId() {
        return userId;
    }

    public List<UUID> getFriendsList() {
        return friendsList;
    }
}
