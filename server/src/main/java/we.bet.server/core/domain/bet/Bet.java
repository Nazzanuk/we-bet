package we.bet.server.core.domain.bet;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

import static we.bet.server.core.domain.bet.Bet.Status.CREATED;

public class Bet {

    @Id
    private UUID id;
    private Date createdDate;
    private UUID createdBy;
    private UUID createdFor;
    private String title;
    private String description;
    private Status status;

    public Bet(UUID createdBy, UUID createdFor, String title, String description) {
        this.id = UUID.randomUUID();
        this.createdDate = new Date();
        this.createdBy = createdBy;
        this.createdFor = createdFor;
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

    public UUID getCreatedBy() {
        return createdBy;
    }

    public UUID getCreatedFor() {
        return createdFor;
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

    public enum Status{
        CREATED, ACCEPTED
    }

}
