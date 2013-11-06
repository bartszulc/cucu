package pl.bartszulc.cucu.service;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bartszulc.cucu.api.user.LoginUserResponse;
import pl.bartszulc.cucu.auth.CucuAuthenticator;
import pl.bartszulc.cucu.service.core.user.User;
import pl.bartszulc.cucu.service.domain.user.UsersService;
import pl.bartszulc.cucu.service.jdbi.user.UserDAO;
import pl.bartszulc.cucu.service.resources.user.UsersResource;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CucuApplication extends Service<CucuConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CucuApplication.class);

    private static final String CUCU_SERVICE = "cucu-service";

    private final HibernateBundle<CucuConfiguration> hibernateBundle =
            new HibernateBundle<CucuConfiguration>(User.class) {
                @Override
                public DatabaseConfiguration getDatabaseConfiguration(CucuConfiguration configuration) {
                    return configuration.getDatabaseConfiguration();
                }
            };

    private final MigrationsBundle<CucuConfiguration> migrationBundle =
            new MigrationsBundle<CucuConfiguration>() {
                @Override
                public DatabaseConfiguration getDatabaseConfiguration(CucuConfiguration configuration) {
                    return configuration.getDatabaseConfiguration();
                }
            };

    public static void main(String[] args) throws Exception {
        LOGGER.debug("Starting main...");
        new CucuApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<CucuConfiguration> cucuConfigurationBootstrap) {
        LOGGER.debug("Initializing the bootstrap...");
        cucuConfigurationBootstrap.setName(CUCU_SERVICE);
        cucuConfigurationBootstrap.addBundle(migrationBundle);
        cucuConfigurationBootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(CucuConfiguration cucuConfiguration, Environment environment) throws Exception {
        LOGGER.debug("Running the service...");
        final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());
        final UsersService usersService = new UsersService(userDAO);
        environment.addResource(new UsersResource(usersService));
        environment.addProvider(new BasicAuthProvider<LoginUserResponse>(new CucuAuthenticator(usersService), CUCU_SERVICE));
    }
}
