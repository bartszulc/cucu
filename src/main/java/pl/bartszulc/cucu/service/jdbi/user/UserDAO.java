package pl.bartszulc.cucu.service.jdbi.user;

import com.yammer.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bartszulc.cucu.service.core.user.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserDAO extends AbstractDAO<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User create(User user) {
        LOGGER.debug("persisting new user...");
        return persist(user);
    }

    public List<User> list() {
        LOGGER.debug("listing all registered users...");
        return list(namedQuery("pl.bartszulc.cucu.service.core.user.User.findAll"));
    }

    public User find(String username, String password) {
        LOGGER.debug("finding registered user: " + username + "/" + password);
        return uniqueResult(namedQuery("pl.bartszulc.cucu.service.core.user.User.find").setParameter("username", username).setParameter("password", password));

    }
}
