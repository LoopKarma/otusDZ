package webserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User {
    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    public User() {};

    public User(long id, String name, String login, String password, String role) {
        this(name, login, password, role);
        this.id = id;
    }

    public User(String name, String login, String password, String role) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() { return role; }
}
