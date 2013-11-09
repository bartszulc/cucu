package pl.bartszulc.cucu.service.core.user;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(
                name = "pl.bartszulc.cucu.service.core.user.User.findAll",
                query = "SELECT u FROM User u"
        ),
        @NamedQuery(
                name = "pl.bartszulc.cucu.service.core.user.User.findByCredentials",
                query = "SELECT u FROM User u WHERE u.username LIKE :username AND u.password LIKE :password"
        ),
        @NamedQuery(
                name = "pl.bartszulc.cucu.service.core.user.User.findById",
                query = "SELECT u FROM User u WHERE u.id LIKE :id"
        )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
