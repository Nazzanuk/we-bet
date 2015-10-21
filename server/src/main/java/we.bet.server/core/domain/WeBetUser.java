package we.bet.server.core.domain;

import org.springframework.data.annotation.Id;

public class WeBetUser {

    @Id
    private String username;
    private String password;

    public WeBetUser() {}

    public WeBetUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return String.format(
                "WeBetUser[username='%s', password='%s']",
                username, password);
    }
}