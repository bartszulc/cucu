package pl.bartszulc.cucu.service.resources.user;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bartszulc.cucu.api.user.LoginUserResponse;
import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.api.user.RegisterUserResponse;
import pl.bartszulc.cucu.service.domain.user.UsersService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * User: bart
 * Date: 11/6/13
 * Time: 4:39 PM
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);

    private final UsersService usersService;

    public UsersResource(UsersService usersService) {
        this.usersService = usersService;
    }

    @POST
    @UnitOfWork
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        LOGGER.debug("incoming register user request: " + registerUserRequest.toString());
        return usersService.register(registerUserRequest);
    }

    @GET
    @UnitOfWork
    public List<RegisterUserResponse> list() {
        LOGGER.debug("incoming list registered user request");
        return usersService.list();
    }

    @GET
    @Path("login")
    @UnitOfWork
    public LoginUserResponse login(@Auth LoginUserResponse loginUserResponse) {
        LOGGER.debug("successful login attempt: " + loginUserResponse);
        return loginUserResponse;
    }
}
