package pl.bartszulc.cucu.service.resources.user;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.jersey.params.LongParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.api.user.UserResponse;
import pl.bartszulc.cucu.service.domain.user.UserService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * User: bart
 * Date: 11/6/13
 * Time: 4:39 PM
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @UnitOfWork
    public UserResponse register(@Valid RegisterUserRequest registerUserRequest) {
        LOGGER.debug("incoming register user request: " + registerUserRequest.toString());
        return userService.register(registerUserRequest);
    }

    @GET
    @UnitOfWork
    public List<UserResponse> list(@Auth UserResponse userResponse) {
        LOGGER.debug("incoming list registered user request");
        return userService.list();
    }

    @GET
    @Path("{user}")
    @UnitOfWork
    public UserResponse details(@Auth UserResponse userResponse, @PathParam("user") LongParam id) {
        return userService.find(id.get());
    }

    @POST
    @Path("{user}")
    @UnitOfWork
    public UserResponse update(@Auth UserResponse userResponse, @PathParam("user") LongParam id, @Valid RegisterUserRequest details) {
        return userService.update(userResponse, id.get(), details);
    }

    @DELETE
    @Path("{user}")
    @UnitOfWork
    public void delete(@Auth UserResponse userResponse, @PathParam("user") LongParam id) {
        userService.delete(userResponse, id.get());
    }

    @GET
    @Path("login")
    @UnitOfWork
    public UserResponse login(@Auth UserResponse userResponse) {
        LOGGER.debug("successful login attempt: " + userResponse);
        return userResponse;
    }
}
