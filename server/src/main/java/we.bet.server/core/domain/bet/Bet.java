package we.bet.server.core.domain.bet;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

import static we.bet.server.core.domain.bet.Bet.Status.CREATED;

public class Bet {

    @Id
    private UUID id;
    private Date createdDate;
    private UUID createdByUserId;
    private UUID createdForUserId;
    private String title;
    private String description;
    private Status status;

    public Bet(UUID createdByUserId, UUID createdForUserId, String title, String description) {
        this.id = UUID.randomUUID();
        this.createdDate = new Date();
        this.createdByUserId = createdByUserId;
        this.createdForUserId = createdForUserId;
        this.title = title;
        this.description = description;
        this.status = CREATED;
    }

    public UUID getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public UUID getCreatedByUserId() {
        return createdByUserId;
    }

    public UUID getCreatedForUserId() {
        return createdForUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status{
        CREATED, ACCEPTED
    }

}
