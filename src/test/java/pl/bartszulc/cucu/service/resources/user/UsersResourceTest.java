package pl.bartszulc.cucu.service.resources.user;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.testing.ResourceTest;
import com.yammer.dropwizard.validation.InvalidEntityException;
import org.junit.Test;
import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.api.user.LoginUserResponse;
import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.api.user.RegisterUserResponse;
import pl.bartszulc.cucu.service.auth.CucuAuthenticator;
import pl.bartszulc.cucu.service.core.user.User;
import pl.bartszulc.cucu.service.domain.user.UsersService;
import pl.bartszulc.cucu.service.helpers.translator.LoginUserTranslator;
import pl.bartszulc.cucu.service.helpers.translator.RegisterUserTranslator;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: bart
 * Date: 11/6/13
 * Time: 8:06 PM
 */
public class UsersResourceTest extends ResourceTest {
    private final User user = new User();
    private final UsersService usersService = mock(UsersService.class);

    public UsersResourceTest() {
        user.setEmail("user@user.pl");
        user.setUsername("user");
        user.setPassword("user");
    }

    @Override
    protected void setUpResources() {
        addResource(new UsersResource(usersService));
        addProvider(new BasicAuthProvider<>(new CucuAuthenticator(usersService), "test-basic-auth"));
    }

    @Test
    public void validUserShouldRegister() {
        // Given
        final RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(user.getEmail());
        registerUserRequest.setPassword(user.getPassword());
        registerUserRequest.setUsername(user.getUsername());
        final RegisterUserResponse registerUserResponse = RegisterUserTranslator.from(user);
        when(usersService.register(registerUserRequest)).thenReturn(registerUserResponse);
        // When
        final RegisterUserResponse response = client().resource("/users").type(MediaType.APPLICATION_JSON).post(RegisterUserResponse.class, registerUserRequest);
        // Then
        verify(usersService).register(registerUserRequest);
        assertThat(response).isEqualTo(registerUserResponse);
    }

    @Test(expected = InvalidEntityException.class)
    public void invalidUserShouldNotRegister() {
        // Given
        final RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("");
        registerUserRequest.setPassword("");
        registerUserRequest.setUsername("");
        // When
        client().resource("/users").type(MediaType.APPLICATION_JSON).post(RegisterUserResponse.class, registerUserRequest);
    }

    @Test
    public void usersShouldBeListed() {
        // Given
        final RegisterUserResponse registerUserResponse = RegisterUserTranslator.from(user);
        final List<RegisterUserResponse> registerUserResponses = new ArrayList<>();
        registerUserResponses.add(registerUserResponse);
        when(usersService.list()).thenReturn(registerUserResponses);
        // When
        final List<RegisterUserResponse> response = client().resource("/users").type(MediaType.APPLICATION_JSON).get(new GenericType<List<RegisterUserResponse>>() {
        });
        // Then
        assertThat(response).containsAll(registerUserResponses);
    }

    @Test
    public void noUsersShouldBeListed() {
        // Given
        final List<RegisterUserResponse> registerUserResponses = new ArrayList<>();
        when(usersService.list()).thenReturn(registerUserResponses);
        // When
        final List<RegisterUserResponse> response = client().resource("/users").type(MediaType.APPLICATION_JSON).get(new GenericType<List<RegisterUserResponse>>() {
        });
        // Then
        assertThat(response).containsAll(registerUserResponses);
    }

    @Test
    public void registeredUserShouldLogin() {
        // Given
        final LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername(user.getUsername());
        loginUserRequest.setPassword(user.getPassword());
        final LoginUserResponse loginUserResponse = LoginUserTranslator.from(user);
        when(usersService.login(loginUserRequest)).thenReturn(loginUserResponse);
        // When
        client().addFilter(new HTTPBasicAuthFilter(user.getUsername(), user.getPassword()));
        final LoginUserResponse response = client().resource("/users/login").type(MediaType.APPLICATION_JSON).get(LoginUserResponse.class);
        // Then
        assertThat(loginUserResponse).isEqualTo(response);
    }

    @Test(expected = UniformInterfaceException.class)
    public void unregisteredUserShouldNotLogin() {
        // Given
        final LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername(user.getUsername());
        loginUserRequest.setPassword(user.getPassword());
        final LoginUserResponse loginUserResponse = LoginUserTranslator.from(user);
        when(usersService.login(loginUserRequest)).thenReturn(loginUserResponse);
        // When
        client().addFilter(new HTTPBasicAuthFilter("unknown", "unknown"));
        client().resource("/users/login").type(MediaType.APPLICATION_JSON).get(LoginUserResponse.class);
    }

    @Test(expected = UniformInterfaceException.class)
    public void noCredentialsShouldNotLogin() {
        // Given
        final LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername(user.getUsername());
        loginUserRequest.setPassword(user.getPassword());
        final LoginUserResponse loginUserResponse = LoginUserTranslator.from(user);
        when(usersService.login(loginUserRequest)).thenReturn(loginUserResponse);
        // When
        client().resource("/users/login").type(MediaType.APPLICATION_JSON).get(LoginUserResponse.class);
    }
}
