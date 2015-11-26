package we.bet.server.core.domain.friend;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

import static we.bet.server.core.domain.friend.FriendRequest.Status.REQUESTED;

public class FriendRequest {

    @Id
    private UUID id;
    private UUID requestedBy;
    private UUID requestedFor;
    private Status status;
    private Date createdDate;

    public FriendRequest(UUID requestedBy, UUID requestedFor) {
        this.id = UUID.randomUUID();
        this.requestedBy = requestedBy;
        this.requestedFor = requestedFor;
        this.status = REQUESTED;
        this.createdDate = new Date();
    }

    public UUID getId() {
        return id;
    }

    public UUID getRequestedBy() {
        return requestedBy;
    }

    public UUID getRequestedFor() {
        return requestedFor;
    }

    public Status getStatus() {
        return status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public enum Status{
        REQUESTED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendRequest that = (FriendRequest) o;

        if (!requestedBy.equals(that.requestedBy)) return false;
        if (!requestedFor.equals(that.requestedFor)) return false;
        return status == that.status;

    }

    @Override
    public int hashCode() {
        int result = requestedBy.hashCode();
        result = 31 * result + requestedFor.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
