package we.bet.server.core.domain.login;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class WeBetUser {

    @Id
    private UUID id;
    private String username;
    private String password;

    public WeBetUser(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username.toLowerCase();
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeBetUser weBetUser = (WeBetUser) o;

        if (!username.equals(weBetUser.username)) return false;
        return password.equals(weBetUser.password);

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}