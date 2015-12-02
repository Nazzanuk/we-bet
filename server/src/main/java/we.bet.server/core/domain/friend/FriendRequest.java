package we.bet.server.core.domain.friend;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

import static we.bet.server.core.domain.friend.FriendRequest.Status.REQUESTED;

public class FriendRequest {

    @Id
    private UUID id;
    private UUID requestedByUserId;
    private UUID requestedForUserId;
    private Status status;
    private Date createdDate;

    public FriendRequest(UUID requestedByUserId, UUID requestedForUserId) {
        this.id = UUID.randomUUID();
        this.requestedByUserId = requestedByUserId;
        this.requestedForUserId = requestedForUserId;
        this.status = REQUESTED;
        this.createdDate = new Date();
    }

    public UUID getId() {
        return id;
    }

    public UUID getRequestedByUserId() {
        return requestedByUserId;
    }

    public UUID getRequestedForUserId() {
        return requestedForUserId;
    }

    public Status getStatus() {
        return status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status{
        REQUESTED, ACCEPTED, DECLINED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendRequest that = (FriendRequest) o;

        if (!requestedByUserId.equals(that.requestedByUserId)) return false;
        if (!requestedForUserId.equals(that.requestedForUserId)) return false;
        return status == that.status;

    }

    @Override
    public int hashCode() {
        int result = requestedByUserId.hashCode();
        result = 31 * result + requestedForUserId.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
