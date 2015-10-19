package we.bet.server.core.domain;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    private String username;
    private String password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[username='%s', password='%s']",
                username, password);
    }

}